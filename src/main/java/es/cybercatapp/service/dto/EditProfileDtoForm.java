package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditProfileDtoForm {

    @NotNull
    @Size(min = 8)

    private String oldPass;

    @NotNull
    @Size(min = 8)
    @Pattern(regexp =  "^.*(?=\\S+$)(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=._]).*$")
    private String newPassword;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
