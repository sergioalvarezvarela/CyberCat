package es.cybercatapp.service.dto;

import java.util.List;

public class ListModuleDtoForm {

    private List<Long> moduleIds;


    public List<Long> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<Long> moduleIds) {
        this.moduleIds = moduleIds;
    }
}
