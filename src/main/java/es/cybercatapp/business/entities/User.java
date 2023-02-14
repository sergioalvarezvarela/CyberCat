package es.cybercatapp.business.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import es.cybercatapp.common.Constants;

@Entity(name = Constants.USER_ENTITY)
@Table(name = Constants.USER_TABLE)
public class User implements Serializable {

    private static final long serialVersionUID = 6887285245743266809L;

    public User() {
    }

    public User(String username,String email, String password, boolean tipo, LocalDateTime fecha_creacion, String imagen_perfil) {
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
    private boolean tipo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fecha_creacion;

    @Column(name = "imagen_perfil", nullable = false)
    private String imagen_perfil;

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

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
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

    @Override
    public String toString() {
        return "User{" +
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
