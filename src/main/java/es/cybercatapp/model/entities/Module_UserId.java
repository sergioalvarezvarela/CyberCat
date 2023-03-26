package es.cybercatapp.model.entities;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Module_UserId implements Serializable {

    private static final long serialVersionUID = 3964287051771255031L;
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "module_id", nullable = false)
    private ModuleId module_id;

    public Module_UserId() {

    }

    public Module_UserId(Long user_id, ModuleId module_id) {
        this.user_id = user_id;
        this.module_id = module_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public ModuleId getModule_id() {
        return module_id;
    }

    public void setModule_id(ModuleId module_id) {
        this.module_id = module_id;
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
