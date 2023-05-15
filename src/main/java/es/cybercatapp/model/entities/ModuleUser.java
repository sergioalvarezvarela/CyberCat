package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.MODULEUSER_ENTITY)
@Table(name = Constants.MODULEUSER_TABLE)
public class ModuleUser {
    @EmbeddedId
    private ModuleUserId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("moduleId")
    private Modules module;


    @Column(name = "completed")
    private Boolean completed;


    public ModuleUser() {
    }

    public ModuleUser(Users users, Modules module, Boolean completed) {
        this.users = users;
        this.module = module;
        this.completed = completed;
        this.id = new ModuleUserId(users.getUserId(),module.getModuleId());
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

    public Modules getModule() {
        return module;
    }

    public void setModule(Modules module) {
        this.module = module;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleUser that = (ModuleUser) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(users, that.users) && Objects.equals(module, that.module);
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
}
