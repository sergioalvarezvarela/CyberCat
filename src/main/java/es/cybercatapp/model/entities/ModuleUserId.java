package es.cybercatapp.model.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/*@Embeddable
public class ModuleUserId implements Serializable {

    private static final long serialVersionUID = 5045976325752371300L;

    private Long userId;

    private ModuleId moduleId;

    public ModuleUserId(Long userId, ModuleId moduleId) {
        this.userId = userId;
        this.moduleId = moduleId;
    }

    public ModuleUserId() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ModuleId getModuleId() {
        return moduleId;
    }

    public void setModuleId(ModuleId moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleUserId that = (ModuleUserId) o;
        return userId.equals(that.userId) && moduleId.equals(that.moduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, moduleId);
    }

    @Override
    public String toString() {
        return "ModuleUserId{" +
                "userId=" + userId +
                ", moduleId=" + moduleId +
                '}';
    }
}*/
