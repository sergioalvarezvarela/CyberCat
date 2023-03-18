package es.cybercatapp.service.dto;

import org.springframework.web.multipart.MultipartFile;

public class UpdateImageProfileDtoForm {

    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
