package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

/*@Entity(name = Constants.MODULEUSER_ENTITY)
@Table(name = Constants.MODULEUSER_TABLE)
public class ModuleUser {
    @EmbeddedId
    private ModuleUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("moduleId")
    private Module module;


    @Column(name = "completed", nullable = false)
    private boolean completed;



    public ModuleUser(){}

    public ModuleUser (Users users, Module module, boolean completed){
        this.users = users;
        this.module = module;
        this.completed = completed;
        this.id = new ModuleUserId(users.getUserId(), module.getId());
    }

    public ModuleUserId getId() {
        return id;
    }

    public void setId(ModuleUserId id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleUser that = (ModuleUser) o;
        return completed == that.completed && id.equals(that.id) && users.equals(that.users) && module.equals(that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, module, completed);
    }

    @Override
    public String toString() {
        return "ModuleUser{" +
                "id=" + id +
                ", users=" + users +
                ", module=" + module +
                ", completed=" + completed +
                '}';
    }
}*/
