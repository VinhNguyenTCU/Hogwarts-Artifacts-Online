package edu.tcu.cs.hogwartsartifactsonline.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "UserEntity name is required.")
    private String username;

    @NotEmpty(message = "Password is required.")
    private String password;

    private boolean enabled;

    @NotEmpty(message = "Roles are required.")
    private String roles; // Space seperated strings
}
