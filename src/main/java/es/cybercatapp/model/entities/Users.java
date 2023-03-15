package es.cybercatapp.model.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import es.cybercatapp.common.Constants;

@Entity(name = Constants.USER_ENTITY)
@Table(name = Constants.USER_TABLE)
public class Users implements Serializable {

    private static final long serialVersionUID = 6887285245743266809L;

    public Users() {
    }

    public Users(String username, String email, String password, Roles tipo, LocalDateTime fecha_creacion, String imagen_perfil) {
        this.username = username;
        this.email = email;
        this.password= password;
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

    @Column(name = "tipo", nullable = false)
    private Roles  tipo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fecha_creacion;

    @Column(name = "imagen_perfil", nullable = true)
    private String imagen_perfil;

    @ManyToMany
    @JoinTable(
            name = "inscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Courses> courses;

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

    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
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
                ", courses=" + courses +
                '}';
    }
}
