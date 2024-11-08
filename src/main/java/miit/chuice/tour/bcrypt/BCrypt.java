package miit.chuice.tour.bcrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static miit.chuice.tour.bcrypt.BCryptData.*;

public class BCrypt {

	private static final int GENERATE_SALT_DEFAULT_LOG2_ROUNDS = 10;
	private static final int BCRYPT_SALT_LEN = 16;

	private static final int BLOWFISH_NUM_ROUNDS = 16;

	private int[] P;
	private int[] S;

	private static String encodeBase64(byte d[], int len)
		throws IllegalArgumentException {
		int off = 0;
		StringBuffer rs = new StringBuffer();
		int c1, c2;

		if (len <= 0 || len > d.length)
			throw new IllegalArgumentException ("Invalid len");

		while (off < len) {
			c1 = d[off++] & 0xff;
			rs.append(BASE_64_CODE[(c1 >> 2) & 0x3f]);
			c1 = (c1 & 0x03) << 4;
			if (off >= len) {
				rs.append(BASE_64_CODE[c1 & 0x3f]);
				break;
			}
			c2 = d[off++] & 0xff;
			c1 |= (c2 >> 4) & 0x0f;
			rs.append(BASE_64_CODE[c1 & 0x3f]);
			c1 = (c2 & 0x0f) << 2;
			if (off >= len) {
				rs.append(BASE_64_CODE[c1 & 0x3f]);
				break;
			}
			c2 = d[off++] & 0xff;
			c1 |= (c2 >> 6) & 0x03;
			rs.append(BASE_64_CODE[c1 & 0x3f]);
			rs.append(BASE_64_CODE[c2 & 0x3f]);
		}
		return rs.toString();
	}

	private static byte char64(char x) {
		if ((int) x > INDEX_64.length)
			return -1;
		return INDEX_64[(int)x];
	}

	private static byte[] decodeBase64(String s, int maxolen)
		throws IllegalArgumentException {
		StringBuffer rs = new StringBuffer();
		int off = 0, slen = s.length(), olen = 0;
		byte ret[];
		byte c1, c2, c3, c4, o;

		if (maxolen <= 0)
			throw new IllegalArgumentException ("Invalid maxolen");

		while (off < slen - 1 && olen < maxolen) {
			c1 = char64(s.charAt(off++));
			c2 = char64(s.charAt(off++));
			if (c1 == -1 || c2 == -1)
				break;
			o = (byte)(c1 << 2);
			o |= (byte) ((c2 & 0x30) >> 4);
			rs.append((char)o);
			if (++olen >= maxolen || off >= slen)
				break;
			c3 = char64(s.charAt(off++));
			if (c3 == -1)
				break;
			o = (byte)((c2 & 0x0f) << 4);
			o |= (byte) ((c3 & 0x3c) >> 2);
			rs.append((char)o);
			if (++olen >= maxolen || off >= slen)
				break;
			c4 = char64(s.charAt(off++));
			o = (byte)((c3 & 0x03) << 6);
			o |= c4;
			rs.append((char)o);
			++olen;
		}

		ret = new byte[olen];
		for (off = 0; off < olen; off++)
			ret[off] = (byte)rs.charAt(off);
		return ret;
	}

	private void encipher(int lr[], int off) {
		int i, n, l = lr[off], r = lr[off + 1];

		l ^= P[0];
		for (i = 0; i <= BLOWFISH_NUM_ROUNDS - 2;) {
			n = S[(l >> 24) & 0xff];
			n += S[0x100 | ((l >> 16) & 0xff)];
			n ^= S[0x200 | ((l >> 8) & 0xff)];
			n += S[0x300 | (l & 0xff)];
			r ^= n ^ P[++i];

			// Feistel substitution on right word
			n = S[(r >> 24) & 0xff];
			n += S[0x100 | ((r >> 16) & 0xff)];
			n ^= S[0x200 | ((r >> 8) & 0xff)];
			n += S[0x300 | (r & 0xff)];
			l ^= n ^ P[++i];
		}
		lr[off] = r ^ P[BLOWFISH_NUM_ROUNDS + 1];
		lr[off + 1] = l;
	}

	private static int streamToWord(byte data[], int offp[]) {
		int i;
		int word = 0;
		int off = offp[0];

		for (i = 0; i < 4; i++) {
			word = (word << 8) | (data[off] & 0xff);
			off = (off + 1) % data.length;
		}

		offp[0] = off;
		return word;
	}

	private void initialiseKey() {
		P = P_ORIG.clone();
		S = S_ORIG.clone();
	}

	private void key(byte key[]) {
		int i;
		int[] koffp = { 0 };
		int[] lr = { 0, 0 };
		int plen = P.length, slen = S.length;

		for (i = 0; i < plen; i++)
			P[i] = P[i] ^ streamToWord(key, koffp);

		for (i = 0; i < plen; i += 2) {
			encipher(lr, 0);
			P[i] = lr[0];
			P[i + 1] = lr[1];
		}

		for (i = 0; i < slen; i += 2) {
			encipher(lr, 0);
			S[i] = lr[0];
			S[i + 1] = lr[1];
		}
	}

	private void eksKey(byte data[], byte key[]) {
		int i;
		int koffp[] = { 0 }, doffp[] = { 0 };
		int lr[] = { 0, 0 };
		int plen = P.length, slen = S.length;

		for (i = 0; i < plen; i++)
			P[i] = P[i] ^ streamToWord(key, koffp);

		for (i = 0; i < plen; i += 2) {
			lr[0] ^= streamToWord(data, doffp);
			lr[1] ^= streamToWord(data, doffp);
			encipher(lr, 0);
			P[i] = lr[0];
			P[i + 1] = lr[1];
		}

		for (i = 0; i < slen; i += 2) {
			lr[0] ^= streamToWord(data, doffp);
			lr[1] ^= streamToWord(data, doffp);
			encipher(lr, 0);
			S[i] = lr[0];
			S[i + 1] = lr[1];
		}
	}

	public byte[] cryptRaw(byte[] password, byte[] salt, int log_rounds,
                           int[] cdata) {
		int rounds, i, j;
		int clen = cdata.length;
		byte[] ret;

		if (log_rounds < 4 || log_rounds > 30)
			throw new IllegalArgumentException ("Bad number of rounds");
		rounds = 1 << log_rounds;
		if (salt.length != BCRYPT_SALT_LEN)
			throw new IllegalArgumentException ("Bad salt length");

		initialiseKey();
		eksKey(salt, password);
		for (i = 0; i != rounds; i++) {
			key(password);
			key(salt);
		}

		for (i = 0; i < 64; i++) {
			for (j = 0; j < (clen >> 1); j++)
				encipher(cdata, j << 1);
		}

		ret = new byte[clen * 4];
		for (i = 0, j = 0; i < clen; i++) {
			ret[j++] = (byte)((cdata[i] >> 24) & 0xff);
			ret[j++] = (byte)((cdata[i] >> 16) & 0xff);
			ret[j++] = (byte)((cdata[i] >> 8) & 0xff);
			ret[j++] = (byte)(cdata[i] & 0xff);
		}
		return ret;
	}

	public static String hashPassword(String password, String salt) {
		BCrypt B;
		String real_salt;
		byte passwordb[], saltb[], hashed[];
		char minor = (char)0;
		int rounds, off = 0;
		StringBuffer rs = new StringBuffer();

		if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
			throw new IllegalArgumentException ("Invalid salt version");
		if (salt.charAt(2) == '$')
			off = 3;
		else {
			minor = salt.charAt(2);
			if (minor != 'a' || salt.charAt(3) != '$')
				throw new IllegalArgumentException ("Invalid salt revision");
			off = 4;
		}

		if (salt.charAt(off + 2) > '$')
			throw new IllegalArgumentException ("Missing salt rounds");
		rounds = Integer.parseInt(salt.substring(off, off + 2));

		real_salt = salt.substring(off + 3, off + 25);
		try {
			passwordb = (password + (minor >= 'a' ? "\000" : "")).getBytes("UTF-8");
		} catch (UnsupportedEncodingException uee) {
			throw new AssertionError("UTF-8 is not supported");
		}

		saltb = decodeBase64(real_salt, BCRYPT_SALT_LEN);

		B = new BCrypt();
		hashed = B.cryptRaw(passwordb, saltb, rounds,
		    BF_CRYPT_CIPHER_TEXT.clone());

		rs.append("$2");
		if (minor >= 'a')
			rs.append(minor);
		rs.append("$");
		if (rounds < 10)
			rs.append("0");
		if (rounds > 30) {
			throw new IllegalArgumentException(
			    "rounds exceeds maximum (30)");
		}
		rs.append(rounds);
		rs.append("$");
		rs.append(encodeBase64(saltb, saltb.length));
		rs.append(encodeBase64(hashed,
		    BF_CRYPT_CIPHER_TEXT.length * 4 - 1));
		return rs.toString();
	}

	public static String generateSalt(int log_rounds, SecureRandom random) {
		StringBuffer rs = new StringBuffer();
		byte rnd[] = new byte[BCRYPT_SALT_LEN];

		random.nextBytes(rnd);

		rs.append("$2a$");
		if (log_rounds < 10)
			rs.append("0");
		if (log_rounds > 30) {
			throw new IllegalArgumentException(
			    "log_rounds exceeds maximum (30)");
		}
		rs.append(log_rounds);
		rs.append("$");
		rs.append(encodeBase64(rnd, rnd.length));
		return rs.toString();
	}

	public static String generateSalt(int log_rounds) {
		return generateSalt(log_rounds, new SecureRandom());
	}

	public static String generateSalt() {
		return generateSalt(GENERATE_SALT_DEFAULT_LOG2_ROUNDS);
	}

	public static boolean equals(String plaintext, String hashed) {
		byte hashed_bytes[];
		byte try_bytes[];
        String try_pw = hashPassword(plaintext, hashed);
        hashed_bytes = hashed.getBytes(StandardCharsets.UTF_8);
        try_bytes = try_pw.getBytes(StandardCharsets.UTF_8);
        if (hashed_bytes.length != try_bytes.length)
			return false;
		byte ret = 0;
		for (int i = 0; i < try_bytes.length; i++)
			ret |= (byte) (hashed_bytes[i] ^ try_bytes[i]);
		return ret == 0;
	}
	public static String hashPassword(String password) {
		return hashPassword(password, BCrypt.generateSalt());
	}
}