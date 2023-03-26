package es.cybercatapp.model.entities;




import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
public class ModuleId implements Serializable {

    private static final long serialVersionUID = -1794466129760681860L;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;
    private Long courseId;


    public ModuleId() {

    }

    public ModuleId( Long moduleId, Long courseId) {
        this.moduleId = moduleId;
        this.courseId = courseId;
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
