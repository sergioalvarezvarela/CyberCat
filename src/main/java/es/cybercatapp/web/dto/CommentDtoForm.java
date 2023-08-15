package es.cybercatapp.web.dto;

import javax.validation.constraints.NotNull;

public class CommentDtoForm {


    private String username;

    private String image;

    private String imageType;
    private String creation_date;

    @NotNull
    private String description;
    @NotNull
    private int grade;

    private String commentary;

    private Long commentId;

    public CommentDtoForm() {
    }

    public CommentDtoForm(String username, String image, String imageType, String creation_date, String description, int grade, String commentary, Long commentId) {
        this.username = username;
        this.image = image;
        this.imageType = imageType;
        this.creation_date = creation_date;
        this.description = description;
        this.grade = grade;
        this.commentary = commentary;
        this.commentId = commentId;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
