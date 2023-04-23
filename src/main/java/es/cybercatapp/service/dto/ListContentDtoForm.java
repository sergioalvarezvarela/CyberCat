package es.cybercatapp.service.dto;

import java.util.List;

public class ListContentDtoForm {

    private List<Long> contentIds;

    public List<Long> getContentIds() {
        return contentIds;
    }

    public void setContentIds(List<Long> contentIds) {
        this.contentIds = contentIds;
    }
}
