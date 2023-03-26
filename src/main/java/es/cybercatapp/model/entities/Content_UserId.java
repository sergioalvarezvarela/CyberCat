package es.cybercatapp.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Content_UserId implements Serializable {

    private static final long serialVersionUID = 6742647725571914638L;
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "content_id", nullable = false)
    private ContentId content_id;

    public Content_UserId() {

    }

    public Content_UserId(Long user_id, ContentId contentId) {
        this.user_id = user_id;
        this.content_id = contentId;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public ContentId getContent_id() {
        return content_id;
    }

    public void setContent_id(ContentId content_id) {
        this.content_id = content_id;
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
