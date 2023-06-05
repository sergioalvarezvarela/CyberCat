package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InscriptionsId implements Serializable {

    private static final long serialVersionUID = 3553952070127293884L;

    private Long userId;

    private Long courseId;

    public InscriptionsId(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public InscriptionsId() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InscriptionsId that = (InscriptionsId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }

    @Override
    public String toString() {
        return "InscriptionsId{" +
                "userId=" + userId +
                ", courseId=" + courseId +
                '}';
    }
}
