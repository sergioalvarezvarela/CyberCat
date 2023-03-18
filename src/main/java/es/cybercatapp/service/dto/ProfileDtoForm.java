package es.cybercatapp.service.dto;

import org.springframework.http.ResponseEntity;

public class ProfileDtoForm {

    private String username;

    private String email;


    private String date;

    private String image;

    private String imageType;


    public ProfileDtoForm(String username, String email, String date, String image, String imageType) {
        this.username=username;
        this.email=email;
        this.date=date;
        this.image = image;
        this.imageType = imageType;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
