package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = Constants.TESTSTRING_TABLE)
@Table(name = Constants.TESTSTRING_ENTITY)
public class StringContent extends Content {
    @Column(name = "enunciado", nullable = false)
    private String enunciado;

    @Column(name = "markdown", nullable = false)
    private Boolean markdown;

    public StringContent() {

    }

    public StringContent(Long contentId, String contentName, int contentPosition, Modules module, String enunciado, Boolean markdown) {
        super(contentId, contentName, contentPosition, module);
        this.enunciado = enunciado;
        this.markdown = markdown;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Boolean getMarkdown() {
        return markdown;
    }

    public void setMarkdown(Boolean markdown) {
        this.markdown = markdown;
    }


}
