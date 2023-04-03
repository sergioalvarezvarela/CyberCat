package es.cybercatapp.service.dto;

import es.cybercatapp.model.entities.Content;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ModuleDtoForm {
    private String id;

    @NotNull
    @Size(min=5)
    private String moduleName;

    private int modulePosition;

    private List<ContentDtoForm> contentList;

    public ModuleDtoForm() {
    }

    public ModuleDtoForm(String id, String moduleName, int modulePosition, List<ContentDtoForm> contentList) {
        this.id = id;
        this.moduleName = moduleName;
        this.modulePosition= modulePosition;
        this.contentList = contentList;
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
