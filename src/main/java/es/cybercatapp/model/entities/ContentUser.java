package es.cybercatapp.model.entities;
/*
import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENTUSER_ENTITY)
@Table(name = Constants.CONTENTUSER_TABLE)
public class ContentUser {

    @EmbeddedId
    private ContentUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contentId")
    private Content content;


    @Column(name = "completed", nullable = false)
    private boolean completed;

    public ContentUser(ContentUserId id, Users users, Content content, boolean completed) {
        this.id = id;
        this.users = users;
        this.content = content;
        this.completed = completed;
    }

    public ContentUser() {

    }

    public ContentUserId getId() {
        return id;
    }

    public void setId(ContentUserId id) {
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
        ContentUser that = (ContentUser) o;
        return completed == that.completed && id.equals(that.id) && users.equals(that.users) && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, content, completed);
    }

    @Override
    public String toString() {
        return "ContentUser{" +
                "id=" + id +
                ", users=" + users +
                ", content=" + content +
                ", completed=" + completed +
                '}';
    }
}*/
