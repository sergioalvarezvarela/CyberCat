package es.cybercatapp.service.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProfileDtoForm {

    @NotNull
    private String username;


    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

    private MultipartFile image;

    private LocalDateTime date;

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
