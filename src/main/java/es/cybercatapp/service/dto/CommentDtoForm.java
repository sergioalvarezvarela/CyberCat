package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CommentDtoForm {


    private LocalDate creation_date;

    @NotNull
    private String description;
    @NotNull
    private int grade;



    private String commentary;

    public CommentDtoForm() {
    }

    public CommentDtoForm(LocalDate creation_date, String description, int grade, String commentary) {
        this.creation_date = creation_date;
        this.description = description;
        this.grade = grade;
        this.commentary = commentary;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
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
}
