package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENT_TABLE)
@Table(name = Constants.CONTENT_ENTITY)
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Content  {
    @EmbeddedId
    private ContentId id;

    @Column(name= "contentname", nullable = false)
    private String contentName;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "moduleId", referencedColumnName = "moduleId", insertable = false, updatable = false),
            @JoinColumn(name = "courseId", referencedColumnName = "course_id", insertable = false, updatable = false)
    })
    private Module module;


    public Content(){}

    public Content(String contentName, Module module) {
        this.contentName = contentName;
        this.module = module;
        this.id = new ContentId(null, module.getId().getModuleId(), module.getId().getCourseId());
    }

    public ContentId getId() {
        return id;
    }

    public void setId(ContentId id) {
        this.id = id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
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
        return Objects.equals(id, content.id) && Objects.equals(contentName, content.contentName) && Objects.equals(module, content.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentName, module);
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", contentName='" + contentName + '\'' +
                ", module=" + module +
                '}';
    }
}
