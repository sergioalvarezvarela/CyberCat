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

    private String image;

    private String imageType;


    public EditProfileDtoForm(String username, String email, String image, String imageType) {
        this.username=username;
        this.email=email;
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
