package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENTUSER_ENTITY)
@Table(name = Constants.CONTENTUSER_TABLE)
public class Content_User {

    @EmbeddedId
    private Content_UserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("content_id")
    private Content content;


    @Column(name = "completed", nullable = false)
    private boolean completed;

    public Content_User(Users users, Content content, boolean completed) {
        this.users = users;
        this.content = content;
        this.completed = completed;
        this.id = new Content_UserId(users.getUserId(), content.getId());
    }

    public Content_User() {

    }

    public Content_UserId getId() {
        return id;
    }

    public void setId(Content_UserId id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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
        Content_User that = (Content_User) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(users, that.users) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, content, completed);
    }

    @Override
    public String toString() {
        return "Content_User{" +
                "id=" + id +
                ", users=" + users +
                ", content=" + content +
                ", completed=" + completed +
                '}';
    }
}
