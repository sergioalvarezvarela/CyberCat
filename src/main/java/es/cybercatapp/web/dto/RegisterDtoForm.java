package es.cybercatapp.web.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDtoForm {

    @NotNull
    @Size(min=6)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;


    @NotNull
    @Size(min = 8, max = 22)
    @Pattern(regexp =  "^.*(?=\\S+$)(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._]).*$")
    private String password;

    @NotNull
    @Email
    private String email;

    private MultipartFile image;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
