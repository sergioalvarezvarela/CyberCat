package es.cybercatapp.web.dto;

import javax.validation.constraints.NotNull;

public class StringCompleteDtoForm {


    Long contentId;


    Long moduleId;


    Long courseId;

    @NotNull
    String enunciado;
    @NotNull
    String frase;

    @NotNull
    String fraseCorrecta;

    @NotNull
    String words;


    public StringCompleteDtoForm(Long contentId, Long moduleId, Long courseId, String enunciado, String frase, String fraseCorrecta, String words) {
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.enunciado = enunciado;
        this.frase = frase;
        this.fraseCorrecta = fraseCorrecta;
        this.words = words;
    }

    public StringCompleteDtoForm() {
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

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getFraseCorrecta() {
        return fraseCorrecta;
    }

    public void setFraseCorrecta(String fraseCorrecta) {
        this.fraseCorrecta = fraseCorrecta;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
