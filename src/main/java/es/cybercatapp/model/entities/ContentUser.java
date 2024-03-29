package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.CONTENTUSER_ENTITY)
@Table(name = Constants.CONTENTUSER_TABLE)
public class ContentUser {

    @EmbeddedId
    private ContentUserId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("contentId")
    private Content content;


    @Column(name = "completed")
    private Boolean completed;

    public ContentUser(Users users, Content content, Boolean completed) {
        this.users = users;
        this.content = content;
        this.completed = completed;
        this.id = new ContentUserId(users.getUserId(), content.getContentId());
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
        ContentUser that = (ContentUser) o;
        return Objects.equals(id, that.id) && Objects.equals(users, that.users) && Objects.equals(content, that.content) && Objects.equals(completed, that.completed);
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
}
