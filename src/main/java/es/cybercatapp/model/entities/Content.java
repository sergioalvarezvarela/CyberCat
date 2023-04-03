package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENT_TABLE)
@Table(name = Constants.CONTENT_ENTITY)
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Content  {

    @EmbeddedId
    private ContentId contentId;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "moduleName", referencedColumnName = "moduleName", insertable = false, updatable = false),
            @JoinColumn(name = "courseId", referencedColumnName = "course_id", insertable = false, updatable = false)
    })
    private Module module;


    public Content(){}

    public Content(String contentName, Module module) {
        this.module = module;
        this.contentId = new ContentId(contentName,module.getId());
    }

    public ContentId getContentId() {
        return contentId;
    }

    public void setContentId(ContentId contentId) {
        this.contentId = contentId;
    }


    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return contentId.equals(content.contentId) && module.equals(content.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId, module);
    }

    @Override
    public String toString() {
        return "Content{" +
                "contentId=" + contentId +
                ", module=" + module +
                '}';
    }
}
