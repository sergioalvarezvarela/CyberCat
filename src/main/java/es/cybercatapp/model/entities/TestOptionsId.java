package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Embeddable
public class TestOptionsId implements Serializable {

    private static final long serialVersionUID = -2469118158475945472L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionsId;

    private Long contentId;

    private Long moduleId;

    private Long courseId;

    public TestOptionsId (){

    }

    public TestOptionsId(Long optionsId, Long contentId, Long moduleId, Long courseId) {
        this.optionsId = optionsId;
        this.contentId = contentId;
        this.moduleId = moduleId;
        this.courseId = courseId;
    }

    public Long getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(Long optionsId) {
        this.optionsId = optionsId;
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
