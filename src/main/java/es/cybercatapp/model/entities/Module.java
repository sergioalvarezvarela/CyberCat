package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity(name = Constants.MODULE_ENTITY)
@Table(name = Constants.MODULE_TABLE)
public class Module implements Serializable {

    private static final long serialVersionUID = -1794466129760681860L;

    public Module(){

    }

    public Module(String module_name, LocalDate module_date, Courses courses ) {
                this.module_name = module_name;
        this.module_date = module_date;
        this.courses = courses;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(name = "module_name", nullable = false)
    private String module_name;

    @Column(name = "module_date", nullable = false)
    private LocalDate module_date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "course_id", nullable = false)
    private Courses courses;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module_User> moduleUsers = new ArrayList<>();

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public LocalDate getModule_date() {
        return module_date;
    }

    public void setModule_date(LocalDate module_date) {
        this.module_date = module_date;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public List<Module_User> getModuleUsers() {
        return moduleUsers;
    }

    public void setModuleUsers(List<Module_User> moduleUsers) {
        this.moduleUsers = moduleUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(moduleId, module.moduleId) && Objects.equals(module_name, module.module_name) && Objects.equals(module_date, module.module_date) && Objects.equals(courses, module.courses) && Objects.equals(moduleUsers, module.moduleUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, module_name, module_date, courses, moduleUsers);
    }
}
