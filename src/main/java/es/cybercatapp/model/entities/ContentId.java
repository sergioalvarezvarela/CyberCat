package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Embeddable
public class ContentId implements Serializable {

    private static final long serialVersionUID = 8451935930613997798L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    private Long moduleId;

    private Long courseId;

    public ContentId (){

    }

    public ContentId(Long contentId, Long moduleId, Long courseId) {
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
