package es.cybercatapp.model.entities;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ModuleId implements Serializable {

    private static final long serialVersionUID = -1794466129760681860L;

    private String moduleName;
    private Long courseId;


    public ModuleId() {

    }

    public ModuleId( String moduleName, Long courseId) {
        this.moduleName = moduleName;
        this.courseId = courseId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
