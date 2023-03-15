package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = Constants.INSCRIPTIONS_ENTITY)
@Table(name = Constants.INSCRIPTIONS_TABLE)
public class Inscriptions implements Serializable {

    private static final long serialVersionUID = -4287354468354377036L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inscriptionid;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user_id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course_id;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    public Long getInscriptionid() {
        return inscriptionid;
    }

    public void setInscriptionid(Long inscriptionid) {
        this.inscriptionid = inscriptionid;
    }

    public Users getUser_id() {
        return user_id;
    }

    public void setUser_id(Users user_id) {
        this.user_id = user_id;
    }

    public Courses getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Courses course_id) {
        this.course_id = course_id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
