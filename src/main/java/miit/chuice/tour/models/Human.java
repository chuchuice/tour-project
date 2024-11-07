package miit.chuice.tour.models;

import miit.chuice.tour.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Human {
    private Long id;
    private Role role;
    private String login;
    private String password;
    private String name;
}
