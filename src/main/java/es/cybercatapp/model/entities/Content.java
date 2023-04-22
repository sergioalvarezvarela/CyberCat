package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENT_TABLE)
@Table(name = Constants.CONTENT_ENTITY)
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Content  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long contentId;

    @Column(name = "contentName", nullable = false)
    private String contentName;

    @Column(name = "contentPosition", nullable = false)
    private int contentPosition;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", nullable = false)
    private Modules module;


    public Content(){}

    public Content(Long contentId, String contentName, int contentPosition, Modules module) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.contentPosition = contentPosition;
        this.module = module;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return contentPosition == content.contentPosition && contentId.equals(content.contentId) && contentName.equals(content.contentName) && Objects.equals(module, content.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId, contentName, contentPosition, module);
    }

    @Override
    public String toString() {
        return "Content{" +
                "contentId=" + contentId +
                ", contentName='" + contentName + '\'' +
                ", contentPosition=" + contentPosition +
                ", module=" + module +
                '}';
    }
}
