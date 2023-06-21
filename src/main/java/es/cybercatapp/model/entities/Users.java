package es.cybercatapp.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import es.cybercatapp.common.Constants;
import org.hibernate.annotations.Cascade;

@Entity(name = Constants.USER_ENTITY)
@Table(name = Constants.USER_TABLE)
public class Users implements Serializable {

    private static final long serialVersionUID = 6887285245743266809L;

    public Users() {
    }

    public Users(String username, String email, String password, Roles tipo, LocalDate fecha_creacion, String imagen_perfil) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
        this.fecha_creacion = fecha_creacion;
        this.imagen_perfil = imagen_perfil;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Roles tipo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fecha_creacion;

    @Column(name = "imagen_perfil")
    private String imagen_perfil;


    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user_owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Courses> courses;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Inscriptions> inscriptions = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ModuleUser> module_users = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<ContentUser> content_users = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Diploma> diplomas = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getTipo() {
        return tipo;
    }

    public void setTipo(Roles tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Inscriptions> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscriptions> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public List<ModuleUser> getModule_users() {
        return module_users;
    }

    public void setModule_users(List<ModuleUser> module_users) {
        this.module_users = module_users;
    }

    public List<ContentUser> getContent_users() {
        return content_users;
    }

    public void setContent_users(List<ContentUser> content_users) {
        this.content_users = content_users;
    }

    public List<Diploma> getDiplomas() {
        return diplomas;
    }

    public void setDiplomas(List<Diploma> diplomas) {
        this.diplomas = diplomas;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(userId, users.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tipo=" + tipo +
                ", fecha_creacion=" + fecha_creacion +
                ", imagen_perfil='" + imagen_perfil + '\'' +
                '}';
    }
}
