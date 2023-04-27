package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TeoricDtoForm {
    @NotNull
    Long contentId;

    Long moduleId;

    Long courseId;
    @NotNull
    @Size(min = 16)
    String markdown;

    public TeoricDtoForm(Long contentId, Long moduleId, Long courseId, String markdown) {
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.markdown = markdown;
    }

    public TeoricDtoForm(){}

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
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
