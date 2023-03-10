package es.cybercatapp.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditProfileDtoForm {


    @NotNull
    @Size(min=6)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;

    @NotNull
    @Email
    private String email;


    public EditProfileDtoForm(String username, String email) {
        this.username=username;
        this.email=email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
