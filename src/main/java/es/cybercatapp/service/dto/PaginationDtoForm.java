package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;

public class PaginationDtoForm {

    @NotNull
    int start;

    @NotNull
    int count;

    public PaginationDtoForm(int start, int count) {
        this.start = start;
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
