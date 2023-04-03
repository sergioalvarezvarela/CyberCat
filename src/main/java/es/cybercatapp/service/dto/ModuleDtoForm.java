package es.cybercatapp.service.dto;

import es.cybercatapp.model.entities.ModuleId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ModuleDtoForm {
    private String id;

    @NotNull
    @Size(min=5)
    private String moduleName;

    private int modulePosition;

    public ModuleDtoForm() {
    }

    public ModuleDtoForm(String id, String moduleName, int modulePosition) {
        this.id = id;
        this.moduleName = moduleName;
        this.modulePosition= modulePosition;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getModulePosition() {
        return modulePosition;
    }

    public void setModulePosition(int modulePosition) {
        this.modulePosition = modulePosition;
    }

}
