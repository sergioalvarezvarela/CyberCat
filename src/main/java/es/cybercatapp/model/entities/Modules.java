package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = Constants.MODULE_ENTITY)
@Table(name = Constants.MODULE_TABLE)
public class Modules implements Serializable {
    private static final long serialVersionUID = -2536823660469037203L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(name = "moduleName", nullable = false)
    private String moduleName;

    @Column(name = "moduleDate", nullable = false)
    private LocalDate moduleDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", nullable = false)
    private Courses courseId;

    @Column(name = "modulePosition", nullable = false)
    private int modulePosition;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy ("contentPosition")
    private List<Content> contents = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "module",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ModuleUser> moduleUsers = new ArrayList<>();


    public Modules(String moduleName, LocalDate moduleDate, Courses courseId, int modulePosition) {
        this.moduleName = moduleName;
        this.moduleDate = moduleDate;
        this.courseId = courseId;
        this.modulePosition = modulePosition;
    }

    public Modules() {

    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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



    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public Courses getCourseId() {
        return courseId;
    }

    public void setCourseId(Courses courseId) {
        this.courseId = courseId;
    }

    public int getModulePosition() {
        return modulePosition;
    }

    public void setModulePosition(int modulePosition) {
        this.modulePosition = modulePosition;
    }

    public List<ModuleUser> getModuleUsers() {
        return moduleUsers;
    }

    public void setModuleUsers(List<ModuleUser> moduleUsers) {
        this.moduleUsers = moduleUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modules modules = (Modules) o;
        return modulePosition == modules.modulePosition && Objects.equals(moduleId, modules.moduleId) && Objects.equals(moduleName, modules.moduleName) && Objects.equals(moduleDate, modules.moduleDate) && Objects.equals(courseId, modules.courseId) && Objects.equals(contents, modules.contents) && Objects.equals(moduleUsers, modules.moduleUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, moduleName, moduleDate, courseId, modulePosition, contents, moduleUsers);
    }

    @Override
    public String toString() {
        return "Modules{" +
                "moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", moduleDate=" + moduleDate +
                ", courseId=" + courseId +
                ", modulePosition=" + modulePosition + '}';
    }
}
