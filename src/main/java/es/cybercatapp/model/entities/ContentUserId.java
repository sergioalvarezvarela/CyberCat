package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContentUserId implements Serializable {
    private static final long serialVersionUID = 3557795495973288591L;
    private Long userId;

    private Long contentId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public ContentUserId(Long userId, Long contentId) {
        this.userId = userId;
        this.contentId = contentId;
    }

    public ContentUserId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentUserId that = (ContentUserId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, contentId);
    }

    @Override
    public String toString() {
        return "ContentUserId{" +
                "userId=" + userId +
                ", contentId=" + contentId +
                '}';
    }
}
