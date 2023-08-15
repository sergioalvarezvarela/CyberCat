package es.cybercatapp.web.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CourseDtoForm {
    private Long id;

    @NotNull
    @Size(min = 5)
    private String courseName;

    @NotNull
    @Min(value = 1)
    private float price;

    @NotNull
    private String category;

    @NotNull
    @Size(min = 10)
    private String description;

    private String image;

    private String imageType;

    private MultipartFile multipartFile;

    private float grade;

    private int totalComments;

    public CourseDtoForm(Long id, String courseName, float price, String category, String description, String image, String imageType, float grade, int totalComments) {
        this.id = id;
        this.courseName = courseName;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.imageType = imageType;
        this.grade = grade;
        this.totalComments = totalComments;
    }

    public CourseDtoForm(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }
}
