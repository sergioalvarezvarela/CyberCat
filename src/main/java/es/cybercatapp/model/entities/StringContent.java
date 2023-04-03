package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = Constants.TESTSTRING_TABLE)
@Table(name = Constants.TESTSTRING_ENTITY)
public class StringContent extends Content {
    @Column(name="enunciado",nullable = false)
    private String enunciado;

    @Column(name="markdown",nullable = false)
    private Boolean markdown;

    public StringContent(){

    }

    public StringContent(String contentName, Module module, String enunciado, Boolean markdown) {
        super(contentName, module);
        this.enunciado = enunciado;
        this.markdown = markdown;
    }

    public String getEnunciado() {
        return enunciado;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StringContent that = (StringContent) o;
        return Objects.equals(enunciado, that.enunciado) && Objects.equals(markdown, that.markdown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enunciado, markdown);
    }


}
