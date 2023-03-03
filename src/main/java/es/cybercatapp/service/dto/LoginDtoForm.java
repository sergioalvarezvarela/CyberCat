package es.cybercatapp.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginDtoForm {

    @NotNull
    @Size(min=1)
    private String username;

    @NotNull
    @Size(min=1)
    private String password;

    private Boolean rememberMe;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
