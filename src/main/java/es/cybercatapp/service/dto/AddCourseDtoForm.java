package es.cybercatapp.service.dto;

import es.cybercatapp.model.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddCourseDtoForm {

    @NotNull
    @Size(min = 5)

    private String courseName;

    @NotNull
    @Min(value = 1)
    private float price;

    @NotNull
    private String category;

    private MultipartFile image;

    @NotNull
    @Size(min = 10)
    private String description;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
