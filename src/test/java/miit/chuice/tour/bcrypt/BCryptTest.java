package miit.chuice.tour.bcrypt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BCryptTest {
    @Test
    void equals() {
        BCryptAPI bCrypt = new BCrypt();
        Assertions.assertTrue(bCrypt.equals("admin", bCrypt.hashPassword("admin")));
    }
}