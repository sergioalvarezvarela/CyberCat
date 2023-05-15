package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ModuleDtoForm {
    private Long id;

    @NotNull
    @Size(min=5)
    private String moduleName;

    private int modulePosition;

    private List<ContentDtoForm> contentList;

    private Boolean completed;

    public ModuleDtoForm() {
    }

    public ModuleDtoForm(Long id, String moduleName, int modulePosition, List<ContentDtoForm> contentList, Boolean completed) {
        this.id = id;
        this.moduleName = moduleName;
        this.modulePosition= modulePosition;
        this.contentList = contentList;
        this.completed = completed;
    }

    public List<ContentDtoForm> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentDtoForm> contentList) {
        this.contentList = contentList;
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

    public int getModulePosition() {
        return modulePosition;
    }

    public void setModulePosition(int modulePosition) {
        this.modulePosition = modulePosition;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
