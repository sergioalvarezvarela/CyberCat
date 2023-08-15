package es.cybercatapp.web.dto;

import javax.validation.constraints.NotNull;

public class TestOptionsDtoForm {


    Long contentId;


    Long moduleId;


    Long courseId;

    @NotNull
    String enunciado;

    @NotNull
    String opcion1;

    @NotNull
    String opcion2;

    @NotNull
    String opcion3;

    @NotNull
    String opcion4;

    @NotNull
    Integer selectedOption;

    public TestOptionsDtoForm() {
    }

    public TestOptionsDtoForm(Long contentId, Long moduleId, Long courseId, String enunciado, String opcion1, String opcion2, String opcion3, String opcion4, Integer selectedOption) {
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.enunciado = enunciado;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.selectedOption = selectedOption;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
        this.selectedOption = selectedOption;
    }
}
