package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ModuleDtoForm {
    private Long id;

    @NotNull
    @Size(min=5)
    private String moduleName;

    public ModuleDtoForm() {
    }

    public ModuleDtoForm(Long id, String moduleName) {
        this.id = id;
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
