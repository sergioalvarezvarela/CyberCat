package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = Constants.MODULE_ENTITY)
@Table(name = Constants.MODULE_TABLE)
public class Module {

    @EmbeddedId
    private ModuleId id;

    @Column(nullable = false)
    private String moduleName;

    @Column(name = "moduleDate", nullable = false)
    private LocalDate moduleDate;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Courses course;

    @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Content> contents = new ArrayList<>();

    public Module(){}

    public Module(String moduleName, LocalDate moduleDate, Courses course)   {
        this.moduleName = moduleName;
        this.moduleDate = moduleDate;
        this.id= new ModuleId(null,course.getCourseId());
    }


    public ModuleId getId() {
        return id;
    }

    public void setId(ModuleId id) {
        this.id = id;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public LocalDate getModuleDate() {
        return moduleDate;
    }

    public void setModuleDate(LocalDate moduleDate) {
        this.moduleDate = moduleDate;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(id, module.id) && Objects.equals(moduleName, module.moduleName) && Objects.equals(moduleDate, module.moduleDate) && Objects.equals(course, module.course) && Objects.equals(contents, module.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moduleName, moduleDate, course, contents);
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", moduleDate=" + moduleDate +
                ", course=" + course +
                ", contents=" + contents +
                '}';
    }
}
