package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContentDtoForm {

    private String moduleName;

    private String contentId;

    @NotNull
    @Size(min=5)
    private String contentName;

    private int contentPosition;

    public ContentDtoForm() {
    }

    public ContentDtoForm(String contentId, String contentName, String moduleName, int contentPosition) {
        this.contentId= contentId;
        this.moduleName = moduleName;
        this.contentName = contentName;
        this.contentPosition = contentPosition;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
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
}
