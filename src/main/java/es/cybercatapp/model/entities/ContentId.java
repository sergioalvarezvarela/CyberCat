package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContentId implements Serializable {

    private static final long serialVersionUID = 8451935930613997798L;

    private String contentName;

    private ModuleId moduleId;

    public ContentId() {

    }

    public ContentId(String contentName, ModuleId moduleId) {
        this.contentName = contentName;
        this.moduleId = moduleId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public ModuleId getModuleId() {
        return moduleId;
    }

    public void setModuleId(ModuleId moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentId contentId = (ContentId) o;
        return contentName.equals(contentId.contentName) && moduleId.equals(contentId.moduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentName, moduleId);
    }


}