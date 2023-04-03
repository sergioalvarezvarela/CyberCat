package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = Constants.MODULE_ENTITY)
@Table(name = Constants.MODULE_TABLE)
public class Module  {
    @EmbeddedId
    private ModuleId id;

    @Column(name = "moduleDate", nullable = false)
    private LocalDate moduleDate;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Courses course;

    @Column(name = "modulePosition", nullable = false)
    private int modulePosition;

    @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Content> contents = new ArrayList<>();


    public Module(String moduleName, LocalDate moduleDate,int modulePosition, Courses course)   {
        this.moduleDate = moduleDate;
        this.course = course;
        this.modulePosition = modulePosition;
        this.id= new ModuleId(moduleName,course.getCourseId());
    }

    public Module() {

    }

    public ModuleId getId() {
        return id;
    }

    public void setId(ModuleId id) {
        this.id = id;
    }


    public LocalDate getModuleDate() {
        return moduleDate;
    }

    public void setModuleDate(LocalDate moduleDate) {
        this.moduleDate = moduleDate;
    }



    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public int getModulePosition() {
        return modulePosition;
    }

    public void setModulePosition(int modulePosition) {
        this.modulePosition = modulePosition;
    }

    /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return moduleName.equals(module.moduleName) && moduleDate.equals(module.moduleDate) && course.equals(module.course) && Objects.equals(contents, module.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, moduleDate, course, contents);
    }*/


}
