package me.tastycake.hackathon_2.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.tastycake.hackathon_2.user.school.SchoolClass;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class User implements Serializable {
    private String name;
    private String mail;
    private String password;
    private SchoolClass schoolClass;

    public User(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    public User(String name, String mail, String password, SchoolClass schoolClass) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.schoolClass = schoolClass;
    }
}
