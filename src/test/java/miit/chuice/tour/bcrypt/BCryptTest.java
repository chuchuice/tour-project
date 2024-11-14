package miit.chuice.tour.bcrypt;

import miit.chuice.tour.security.bcrypt.BCrypt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BCryptTest {
    @Test void equals() {
        Assertions.assertTrue(BCrypt.equals("admin", BCrypt.hashPassword("admin")));
    }
}