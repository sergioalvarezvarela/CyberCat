package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = Constants.DIPLOMA_ENTITY)
@Table(name = Constants.DIPLOMA_TABLE)
public class Diploma implements Serializable {

    private static final long serialVersionUID = 7570670593998211150L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diplomaId;

    @Column(name = "pdf", nullable = false, unique = true)
    private String pdf;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", nullable = false)
    private Courses courses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    public Diploma(String pdf, Courses courses, Users users) {
        this.pdf = pdf;
        this.courses = courses;
        this.users = users;
    }

    public Diploma() {

    }

    public Long getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(Long diplomaId) {
        this.diplomaId = diplomaId;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Diploma other = (Diploma) obj;
        if (getDiplomaId() == null) {
            if (other.getDiplomaId() != null)
                return false;
        } else if (!getDiplomaId().equals(other.getDiplomaId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDiplomaId() == null) ? 0 : getDiplomaId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Diploma{" +
                "diplomaId=" + diplomaId +
                ", pdf='" + pdf + '\'' +
                '}';
    }
}
