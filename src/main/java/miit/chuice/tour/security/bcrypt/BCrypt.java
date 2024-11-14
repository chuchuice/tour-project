package miit.chuice.tour.security.bcrypt;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Реализация BCrypt хеширования паролей.
 * @author Damien Miller, Pavel Zuev
 */
public class BCrypt extends BCryptData {

	private static int[] permutation;
	private static int[] substitution;

	private static final int DEFAULT_LOG2_ROUNDS = 10;
	private static final int SALT_LENGTH = 16;
	private static final int BLOWFISH_ROUNDS = 16;

	/**
	 * Кодирует массив байтов в строку Base64.
	 *
	 * @param data массив байтов для кодирования
	 * @param length длина данных для кодирования
	 * @return строка Base64
	 * @throws IllegalArgumentException если длина меньше нуля, либо больше, чем длина массива байтов для кодирования
	 */

	private static String encodeBase64(byte[] data, int length)  {
		int offset = 0;
		StringBuilder result = new StringBuilder();
		int firstByte, secondByte;

		if (length <= 0 || length > data.length)
			throw new IllegalArgumentException("Invalid length");

		while (offset < length) {
			firstByte = data[offset++] & 0xff;
			result.append(BASE_64_CODE[(firstByte >> 2) & 0x3f]);
			firstByte = (firstByte & 0x03) << 4;
			if (offset >= length) {
				result.append(BASE_64_CODE[firstByte & 0x3f]);
				break;
			}
			secondByte = data[offset++] & 0xff;
			firstByte |= (secondByte >> 4) & 0x0f;
			result.append(BASE_64_CODE[firstByte & 0x3f]);
			firstByte = (secondByte & 0x0f) << 2;
			if (offset >= length) {
				result.append(BASE_64_CODE[firstByte & 0x3f]);
				break;
			}
			secondByte = data[offset++] & 0xff;
			firstByte |= (secondByte >> 6) & 0x03;
			result.append(BASE_64_CODE[firstByte & 0x3f]);
			result.append(BASE_64_CODE[secondByte & 0x3f]);
		}
		return result.toString();
	}

	/**
	 * Преобразует символ Base64 в байт.
	 *
	 * @param character символ Base64
	 * @return байт, соответствующий символу Base64, или -1, если введён некорректный символ
	 */

	private static byte char64(char character) {
		if ((int) character > INDEX_64.length)
			return -1;
		return INDEX_64[character];
	}

	/**
	 * Декодирует строку Base64 в массив байтов.
	 *
	 * @param encodedString закодированная строка Base64
	 * @return массив байтов
	 * @throws IllegalArgumentException если максимальная длина выходных данных некорректна
	 */

	private static byte[] decodeBase64(String encodedString)
			throws IllegalArgumentException {
		StringBuilder result = new StringBuilder();
		int offset = 0, stringLength = encodedString.length(), outputLength = 0;
		byte[] output;
		byte firstChar, secondChar, thirdChar, fourthChar, outputChar;

		if (BCrypt.SALT_LENGTH <= 0)
			throw new IllegalArgumentException("Invalid maxOutputLength");

		while (offset < stringLength - 1 && outputLength < BCrypt.SALT_LENGTH) {
			firstChar = char64(encodedString.charAt(offset++));
			secondChar = char64(encodedString.charAt(offset++));
			if (firstChar == -1 || secondChar == -1)
				break;
			outputChar = (byte) (firstChar << 2);
			outputChar |= (byte) ((secondChar & 0x30) >> 4);
			result.append((char) outputChar);
			if (++outputLength >= BCrypt.SALT_LENGTH || offset >= stringLength)
				break;
			thirdChar = char64(encodedString.charAt(offset++));
			if (thirdChar == -1)
				break;
			outputChar = (byte) ((secondChar & 0x0f) << 4);
			outputChar |= (byte) ((thirdChar & 0x3c) >> 2);
			result.append((char) outputChar);
			if (++outputLength >= BCrypt.SALT_LENGTH || offset >= stringLength)
				break;
			fourthChar = char64(encodedString.charAt(offset++));
			outputChar = (byte) ((thirdChar & 0x03) << 6);
			outputChar |= fourthChar;
			result.append((char) outputChar);
			++outputLength;
		}

		output = new byte[outputLength];
		for (offset = 0; offset < outputLength; offset++)
			output[offset] = (byte) result.charAt(offset);
		return output;
	}

	/**
	 * Шифрует данные с использованием алгоритма Blowfish.
	 *
	 * @param leftRight массив, содержащий левую и правую части данных
	 * @param offset смещение в массиве
	 */

	private static void encipher(int[] leftRight, int offset) {
		int left = leftRight[offset];
		int right = leftRight[offset + 1];

		left ^= permutation[0];

		for (int i = 0; i <= BLOWFISH_ROUNDS - 2; i += 2) {
			right ^= f(left) ^ permutation[i + 1];
			left ^= f(right) ^ permutation[i + 2];
		}

		leftRight[offset] = right ^ permutation[BLOWFISH_ROUNDS + 1];
		leftRight[offset + 1] = left;
	}

	/**
	 * Вычисляет значение функции f для алгоритма Blowfish.
	 *
	 * @param x входное значение
	 * @return результат функции f
	 */

	private static int f(int x) {
		int n = substitution[(x >> 24) & 0xff];
		n += substitution[0x100 | ((x >> 16) & 0xff)];
		n ^= substitution[0x200 | ((x >> 8) & 0xff)];
		n += substitution[0x300 | (x & 0xff)];
		return n;
	}

	/**
	 * Преобразует поток байтов в слово.
	 *
	 * @param data массив байтов
	 * @param offsetPointer указатель на смещение
	 * @return слово
	 */

	private static int streamToWord(byte[] data, int[] offsetPointer) {
		int i;
		int word = 0;
		int offset = offsetPointer[0];

		for (i = 0; i < 4; i++) {
			word = (word << 8) | (data[offset] & 0xff);
			offset = (offset + 1) % data.length;
		}

		offsetPointer[0] = offset;
		return word;
	}

	/**
	 * Инициализирует ключ для алгоритма Blowfish.
	 */

	private static void initializeKey() {
		permutation = PERMUTATION_BOX.clone();
		substitution = SUBSTITUTION_BOX.clone();
	}

	/**
	 * Устанавливает ключ для алгоритма Blowfish.
	 *
	 * @param key ключ
	 */

	private static void key(byte[] key) {
		int i;
		int[] keyOffsetPointer = {0};
		int[] leftRight = {0, 0};
		int permutationLength = permutation.length, substitutionLength = substitution.length;

		for (i = 0; i < permutationLength; i++)
			permutation[i] = permutation[i] ^ streamToWord(key, keyOffsetPointer);

		for (i = 0; i < permutationLength; i += 2) {
			encipher(leftRight, 0);
			permutation[i] = leftRight[0];
			permutation[i + 1] = leftRight[1];
		}

		for (i = 0; i < substitutionLength; i += 2) {
			encipher(leftRight, 0);
			substitution[i] = leftRight[0];
			substitution[i + 1] = leftRight[1];
		}
	}

	/**
	 * Устанавливает расширенный ключ для алгоритма Blowfish.
	 *
	 * @param data данные
	 * @param key ключ
	 */

	private static void eksKey(byte[] data, byte[] key) {
		int i;
		int[] keyOffsetPointer = {0};
		int[] dataOffsetPointer = {0};
		int[] leftRight = new int[2];
		int permutationLength = permutation.length;
		int substitutionLength = substitution.length;

		for (i = 0; i < permutationLength; i++) {
			permutation[i] ^= streamToWord(key, keyOffsetPointer);
		}

		processData(data, dataOffsetPointer, leftRight, permutation, permutationLength);
		processData(data, dataOffsetPointer, leftRight, substitution, substitutionLength);
	}

	/**
	 * Обрабатывает данные для алгоритма Blowfish.
	 *
	 * @param data данные
	 * @param dataOffsetPointer указатель на смещение данных
	 * @param leftRight массив, содержащий левую и правую части данных
	 * @param array массив для обработки
	 * @param length длина массива
	 */

	private static void processData(byte[] data, int[] dataOffsetPointer, int[] leftRight, int[] array, int length) {
		for (int i = 0; i < length; i += 2) {
			leftRight[0] ^= streamToWord(data, dataOffsetPointer);
			leftRight[1] ^= streamToWord(data, dataOffsetPointer);
			encipher(leftRight, 0);
			array[i] = leftRight[0];
			array[i + 1] = leftRight[1];
		}
	}

	/**
	 * Хэширует пароль.
	 *
	 * @param password пароль
	 * @param salt соль
	 * @param logRounds количество раундов (логарифмическое)
	 * @param cipherData данные шифра
	 * @return зашифрованные данные
	 * @throws IllegalArgumentException если количество раундов или длина соли недействительны
	 */

	private static byte[] cryptRaw(byte[] password, byte[] salt, int logRounds, int[] cipherData) {
		int rounds, i, j;
		int cipherDataLength = cipherData.length;
		byte[] result;

		if (logRounds < 4 || logRounds > 30)
			throw new IllegalArgumentException("Bad number of rounds");
		rounds = 1 << logRounds;
		if (salt.length != SALT_LENGTH)
			throw new IllegalArgumentException("Bad salt length");

		initializeKey();
		eksKey(salt, password);
		for (i = 0; i != rounds; i++) {
			key(password);
			key(salt);
		}

		for (i = 0; i < 64; i++) {
			for (j = 0; j < (cipherDataLength >> 1); j++)
				encipher(cipherData, j << 1);
		}

		result = new byte[cipherDataLength * 4];
		for (i = 0, j = 0; i < cipherDataLength; i++) {
			result[j++] = (byte) ((cipherData[i] >> 24) & 0xff);
			result[j++] = (byte) ((cipherData[i] >> 16) & 0xff);
			result[j++] = (byte) ((cipherData[i] >> 8) & 0xff);
			result[j++] = (byte) (cipherData[i] & 0xff);
		}
		return result;
	}

	/**
	 * Хеширует пароль, метод для пользователя (API)
	 *
	 * @param password пароль
	 * @param salt соль
	 * @return хешированный пароль
	 * @throws IllegalArgumentException если версия или ревизия соли некорректна
	 */
	private static String hashPassword(String password, String salt) {
		String realSalt;
		byte[] passwordBytes;
		byte[] saltBytes;
		byte[] hashed;

		char minorVersion = (char) 0;
		int rounds, offset;
		StringBuilder result = new StringBuilder();

		if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
			throw new IllegalArgumentException("Invalid salt version");
		if (salt.charAt(2) == '$')
			offset = 3;
		else {
			minorVersion = salt.charAt(2);
			if (minorVersion != 'a' || salt.charAt(3) != '$')
				throw new IllegalArgumentException("Invalid salt revision");
			offset = 4;
		}

		if (salt.charAt(offset + 2) > '$')
			throw new IllegalArgumentException("Missing salt rounds");
		rounds = Integer.parseInt(salt.substring(offset, offset + 2));

		realSalt = salt.substring(offset + 3, offset + 25);
		passwordBytes = (password + (minorVersion == 'a' ? "\000" : "")).getBytes(StandardCharsets.UTF_8);

		saltBytes = decodeBase64(realSalt);

		hashed = BCrypt.cryptRaw(passwordBytes, saltBytes, rounds, BLOWFISH_CRYPT_CIPHER_TEXT.clone());

		result.append("$2");
		if (minorVersion >= 'a')
			result.append(minorVersion);
		result.append("$");
		if (rounds < 10)
			result.append("0");
		if (rounds > 30) {
			throw new IllegalArgumentException(
					"rounds exceeds maximum (30)");
		}
		result.append(rounds);
		result.append("$");
		result.append(encodeBase64(saltBytes, saltBytes.length));
		result.append(encodeBase64(hashed, BLOWFISH_CRYPT_CIPHER_TEXT.length * 4 - 1));
		return result.toString();
	}

	/**
	 * Генерирует соль для алгоритма BCrypt.
	 *
	 * @param logRounds количество раундов (логарифмическое)
	 * @param random генератор случайных чисел
	 * @return сгенерированная соль
	 * @throws IllegalArgumentException если количество раундов превышает максимальное значение
	 */

	private static String generateSalt(int logRounds, SecureRandom random) {
		StringBuilder result = new StringBuilder();
		byte[] randomBytes = new byte[SALT_LENGTH];

		random.nextBytes(randomBytes);

		result.append("$2a$");
		if (logRounds < 10)
			result.append("0");
		if (logRounds > 30) {
			throw new IllegalArgumentException(
					"logRounds exceeds maximum (30)");
		}
		result.append(logRounds);
		result.append("$");
		result.append(encodeBase64(randomBytes, randomBytes.length));
		return result.toString();
	}

	/**
	 * Генерирует соль для алгоритма BCrypt с использованием заданного количества раундов.
	 *
	 * @param logRounds количество раундов (логарифмическое)
	 * @return сгенерированная соль
	 */
	private static String generateSalt(int logRounds) {
		return generateSalt(logRounds, new SecureRandom());
	}

	/**
	 * Генерирует соль для алгоритма BCrypt с использованием количества раундов по умолчанию.
	 *
	 * @return сгенерированная соль
	 */
	private static String generateSalt() {
		return generateSalt(DEFAULT_LOG2_ROUNDS);
	}

	/**
	 * Проверяет, совпадает ли хешированный пароль с исходным паролем.
	 *
	 * @param plaintext исходный пароль
	 * @param hashed хешированный пароль
	 * @return true, если пароли совпадают, иначе false
	 */
	public static boolean equals(String plaintext, String hashed) {
		byte[] hashedBytes;
		byte[] tryBytes;
		String tryPassword = hashPassword(plaintext, hashed);
		hashedBytes = hashed.getBytes(StandardCharsets.UTF_8);
		tryBytes = tryPassword.getBytes(StandardCharsets.UTF_8);
		if (hashedBytes.length != tryBytes.length)
			return false;
		byte result = 0;
		for (int i = 0; i < tryBytes.length; i++)
			result |= (byte) (hashedBytes[i] ^ tryBytes[i]);
		return result == 0;
	}

	/**
	 * Хеширует пароль с использованием алгоритма BCrypt с использованием количества раундов по умолчанию.
	 * API.
	 * @param password пароль
	 * @return хешированный пароль
	 */
	public static String hashPassword(String password) {
		return hashPassword(password, generateSalt());
	}

	/**
	 * Хеширует пароль с использованием алгоритма BCrypt с использованием заданного количества раундов.
	 *
	 * @param password пароль
	 * @param logRounds количество раундов (логарифмическое)
	 * @return хешированный пароль
	 */
	public static String hashPassword(String password, int logRounds) {
		return hashPassword(password, generateSalt(logRounds));
	}

}