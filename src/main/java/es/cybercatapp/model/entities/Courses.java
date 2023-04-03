package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = Constants.COURSE_ENTITY)
@Table(name = Constants.COURSE_TABLE)
public class Courses implements Serializable {

    private static final long serialVersionUID = -4787752563093440411L;

    public Courses() {
    }

    public Courses(String course_name, String course_description, LocalDate creation_date, String course_photo, Category course_category, float course_price, Users user_owner) {
        this.course_name = course_name;
        this.course_description = course_description;
        this.creation_date = creation_date;
        this.course_photo = course_photo;
        this.course_category = course_category;
        this.course_price = course_price;
        this.user_owner = user_owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String course_name;

    @Column(name = "course_description", nullable = false)
    private String course_description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creation_date;

    @Column(name = "course_photo", nullable = false)
    private String course_photo;
    @Enumerated(EnumType.STRING)
    @Column(name = "course_category", nullable = false)
    private Category course_category;

    @Column(name = "course_price", nullable = false)
    private float course_price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user_owner;

    @OneToMany(
            mappedBy = "courses",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Inscriptions> inscriptions = new ArrayList<>();

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("modulePosition")
    private List<Module> modules = new ArrayList<>();


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public String getCourse_photo() {
        return course_photo;
    }

    public void setCourse_photo(String course_photo) {
        this.course_photo = course_photo;
    }


    public Users getUser_owner() {
        return user_owner;
    }

    public void setUser_owner(Users user_owner) {
        this.user_owner = user_owner;
    }

    public Category getCourse_category() {
        return course_category;
    }

    public void setCourse_category(Category course_category) {
        this.course_category = course_category;
    }

    public float getCourse_price() {
        return course_price;
    }

    public void setCourse_price(float course_price) {
        this.course_price = course_price;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Inscriptions> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscriptions> inscriptions) {
        this.inscriptions = inscriptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courses courses = (Courses) o;
        return Float.compare(courses.course_price, course_price) == 0 && Objects.equals(courseId, courses.courseId) && Objects.equals(course_name, courses.course_name) && Objects.equals(course_description, courses.course_description) && Objects.equals(creation_date, courses.creation_date) && Objects.equals(course_photo, courses.course_photo) && course_category == courses.course_category && Objects.equals(user_owner, courses.user_owner) && Objects.equals(inscriptions, courses.inscriptions) && Objects.equals(modules, courses.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, course_name, course_description, creation_date, course_photo, course_category, course_price, user_owner, inscriptions, modules);
    }

    @Override
    public String toString() {
        return "Courses{" +
                "courseId=" + courseId +
                ", course_name='" + course_name + '\'' +
                ", course_description='" + course_description + '\'' +
                ", creation_date=" + creation_date +
                ", course_photo='" + course_photo + '\'' +
                ", course_category=" + course_category +
                ", course_price=" + course_price +
                ", user_owner=" + user_owner +
                ", inscriptions=" + inscriptions +
                ", modules=" + modules +
                '}';
    }
}
