package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContentDtoForm {

    private String moduleName;

    private Long contentId;

    @NotNull
    @Size(min=5)
    private String contentName;

    private int contentPosition;

    private String contentType;

    private Boolean completed;


    public ContentDtoForm() {
    }

    public ContentDtoForm(Long contentId,String contentName, String moduleName, int contentPosition, String contentType, Boolean completed) {
        this.moduleName = moduleName;
        this.contentId = contentId;
        this.contentName = contentName;
        this.contentPosition = contentPosition;
        this.contentType = contentType;
        this.completed = completed;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public int getContentPosition() {
        return contentPosition;
    }

    public void setContentPosition(int contentPosition) {
        this.contentPosition = contentPosition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
