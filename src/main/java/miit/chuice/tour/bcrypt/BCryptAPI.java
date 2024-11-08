package miit.chuice.tour.bcrypt;

import org.springframework.stereotype.Component;

@Component
public interface BCryptAPI {
    String hashPassword(String password);
    String hashPassword(String password, int logRounds);
    boolean equals(String plaintext, String hashed);
}