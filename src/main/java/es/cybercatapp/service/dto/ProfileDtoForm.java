package es.cybercatapp.service.dto;

import org.springframework.http.ResponseEntity;

public class ProfileDtoForm {

    private String username;

    private String email;


    private String date;

    private byte[] image;


    public ProfileDtoForm(String username, String email, String date, byte[] image) {
        this.username=username;
        this.email=email;
        this.date=date;
        this.image = image;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
