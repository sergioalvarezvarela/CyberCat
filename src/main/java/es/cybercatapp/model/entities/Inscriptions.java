package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.INSCRIPTIONS_ENTITY)
@Table(name = Constants.INSCRIPTIONS_TABLE)
public class Inscriptions  {

    @EmbeddedId
    private InscriptionsId id;
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private Users users;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("courseId")
    private Courses courses;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    public Inscriptions(){}

    public Inscriptions (Users users, Courses courses, boolean completed){
        this.users = users;
        this.courses = courses;
        this.completed = completed;
        this.id = new InscriptionsId(users.getUserId(),courses.getCourseId());
    }


    public InscriptionsId getId() {
        return id;
    }

    public void setId(InscriptionsId id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
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
        Inscriptions that = (Inscriptions) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(users, that.users) && Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, courses, completed);
    }

    @Override
    public String toString() {
        return "Inscriptions{" +
                "id=" + id +
                ", users=" + users +
                ", courses=" + courses +
                ", completed=" + completed +
                '}';
    }
}
