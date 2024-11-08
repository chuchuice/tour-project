package miit.chuice.tour.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import miit.chuice.tour.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Human {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public Human(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }
}