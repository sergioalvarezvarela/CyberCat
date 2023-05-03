package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TeoricDtoForm {
    @NotNull
    Long contentId;

    @NotNull
    Long moduleId;

    @NotNull
    Long courseId;
    @NotNull
    @Size(min = 16)
    String html;


    public TeoricDtoForm(Long contentId, Long moduleId, Long courseId, String html, String type) {
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.html = html;
    }

    public TeoricDtoForm(){}

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

}
