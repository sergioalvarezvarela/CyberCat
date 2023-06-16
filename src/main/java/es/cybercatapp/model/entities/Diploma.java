package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = Constants.DIPLOMA_ENTITY)
@Table(name = Constants.DIPLOMA_TABLE)
public class Diploma implements Serializable {

    private static final long serialVersionUID = 7570670593998211150L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diplomaId;

    @Column(name = "pdf", nullable = false)
    private String pdf;

    @Column(name = "fobtencion", nullable = false)
    private LocalDate fobtencion;

    @Column(name = "paymentCompleted", nullable = false)
    private boolean paymentCompleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", nullable = false)
    private Courses courses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    public Diploma(String pdf, LocalDate fobtencion, boolean paymentCompleted, Courses courses, Users users) {
        this.pdf = pdf;
        this.fobtencion = fobtencion;
        this.paymentCompleted = paymentCompleted;
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

    public LocalDate getFobtencion() {
        return fobtencion;
    }

    public void setFobtencion(LocalDate fobtencion) {
        this.fobtencion = fobtencion;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diploma diploma = (Diploma) o;
        return paymentCompleted == diploma.paymentCompleted && Objects.equals(diplomaId, diploma.diplomaId) && Objects.equals(pdf, diploma.pdf) && Objects.equals(fobtencion, diploma.fobtencion) && Objects.equals(courses, diploma.courses) && Objects.equals(users, diploma.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diplomaId, pdf, fobtencion, paymentCompleted, courses, users);
    }

    @Override
    public String toString() {
        return "Diploma{" +
                "diplomaId=" + diplomaId +
                ", pdf='" + pdf + '\'' +
                ", fobtencion=" + fobtencion +
                ", paymentCompleted=" + paymentCompleted +
                ", courses=" + courses +
                ", users=" + users +
                '}';
    }
}
