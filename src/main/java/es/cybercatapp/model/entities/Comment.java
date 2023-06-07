package es.cybercatapp.model.entities;


import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = Constants.COMMENT_ENTITY)
@Table(name = Constants.COMMENT_TABLE)
public class Comment implements Serializable {

    private static final long serialVersionUID = -7418219361959443956L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long commentId;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "creation_date", nullable = false)
    private LocalDate creation_date;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "commentary", columnDefinition = "LONGTEXT")
    private String commentary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", nullable = false)
    private Courses courses;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    public Comment(String description, LocalDate creation_date, int grade, String commentary, Courses courses, Users users) {
        this.description = description;
        this.creation_date = creation_date;
        this.grade = grade;
        this.commentary = commentary;
        this.courses = courses;
        this.users = users;
    }

    public Comment() {
    }


    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (commentId == null) {
            if (other.getCommentId() != null)
                return false;
        } else if (!commentId.equals(other.getCommentId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", description='" + description + '\'' +
                ", creation_date=" + creation_date +
                ", grade=" + grade +
                ", commentary='" + commentary + '\'' +
                ", courses=" + courses +
                ", users=" + users +
                '}';
    }
}
