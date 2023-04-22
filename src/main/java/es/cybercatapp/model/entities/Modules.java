package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = Constants.MODULE_ENTITY)
@Table(name = Constants.MODULE_TABLE)
public class Module implements Serializable {
    private static final long serialVersionUID = -2536823660469037203L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(name = "moduleName", nullable = false)
    private String moduleName;

    @Column(name = "moduleDate", nullable = false)
    private LocalDate moduleDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Courses courseId;

    @Column(name = "modulePosition", nullable = false)
    private int modulePosition;

   /* @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Content> contents = new ArrayList<>();*/


    public Module(String moduleName, LocalDate moduleDate, Courses courseId, int modulePosition) {
        this.moduleName = moduleName;
        this.moduleDate = moduleDate;
        this.courseId = courseId;
        this.modulePosition = modulePosition;
    }

    public Module() {

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



    /*public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }*/

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return modulePosition == module.modulePosition && moduleId.equals(module.moduleId) && moduleName.equals(module.moduleName) && moduleDate.equals(module.moduleDate) && courseId.equals(module.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, moduleName, moduleDate, courseId, modulePosition);
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", moduleDate=" + moduleDate +
                ", courseId=" + courseId +
                ", modulePosition=" + modulePosition +
                '}';
    }
}
