package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.MODULEUSER_ENTITY)
@Table(name = Constants.MODULEUSER_TABLE)
public class Module_User {
    @EmbeddedId
    private Module_UserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("module_id")
    private Module module;


    @Column(name = "completed", nullable = false)
    private boolean completed;



    public Module_User(){}

    public Module_User (Users users, Module module, boolean completed){
        this.users = users;
        this.module = module;
        this.completed = completed;
        this.id = new Module_UserId(users.getUserId(), module.getId());
    }

    public Module_UserId getId() {
        return id;
    }

    public void setId(Module_UserId id) {
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
        Module_User that = (Module_User) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(users, that.users) && Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, module, completed);
    }
}
