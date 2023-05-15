package es.cybercatapp.service.dto;

import javax.validation.constraints.NotNull;

public class CatalogDtoForm {

    @NotNull
    int start;


    @NotNull
    int filter;

    @NotNull
    String word;
    @NotNull
    String category;

    public CatalogDtoForm(int start, int filter, String word, String category) {
        this.start = start;
        this.filter = filter;
        this.word = word;
        this.category = category;
    }

    public CatalogDtoForm() {
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
