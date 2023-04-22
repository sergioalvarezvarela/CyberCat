package es.cybercatapp.model.entities;

import javax.persistence.*;
import java.io.Serializable;
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

    public Users(String username, String email, String password, Roles tipo, LocalDateTime fecha_creacion, String imagen_perfil) {
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
    private LocalDateTime fecha_creacion;

    @Column(name = "imagen_perfil")
    private String imagen_perfil;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user_owner")
    private Set<Courses> courses;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Inscriptions> inscriptions = new ArrayList<>();

  /*  @OneToMany(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Module_User> module_users = new ArrayList<>();

    @OneToMany(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Content_User> content_users = new ArrayList<>();*/


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

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
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
        Users users = (Users) o;
        return userId.equals(users.userId) && username.equals(users.username) && email.equals(users.email) && password.equals(users.password) && tipo == users.tipo && fecha_creacion.equals(users.fecha_creacion) && imagen_perfil.equals(users.imagen_perfil) && Objects.equals(courses, users.courses) && Objects.equals(inscriptions, users.inscriptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, password, tipo, fecha_creacion, imagen_perfil, courses, inscriptions);
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
