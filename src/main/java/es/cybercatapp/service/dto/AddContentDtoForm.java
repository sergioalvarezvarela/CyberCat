package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddContentDtoForm {
    @NotNull
    @Size(min=5)
    private String contentName;

    private String contentType;
    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
