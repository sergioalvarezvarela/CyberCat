package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
public class InscriptionsId implements Serializable {

    private static final long serialVersionUID = -4287354468354377036L;

    @Column(name = "user_id", nullable = false)
    private Long user_id;


    @Column(name = "course_id", nullable = false)
    private Long course_id;


    public InscriptionsId() {

    }
    public InscriptionsId(Long user_id, Long course_id) {
        this.user_id = user_id;
        this.course_id = course_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
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
