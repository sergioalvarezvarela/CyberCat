package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = Constants.STRINGCOMPLETE_ENTITY)
@Table(name = Constants.STRINGCOMPLETE_TABLE)
public class StringComplete extends Content  {

    private static final long serialVersionUID = -3757841763488638573L;

    @Column(name = "enunciado", nullable = false)
    private String enunciado;
    @Lob
    @Column(name = "sentence", nullable = false, length = Integer.MAX_VALUE)
    private String sentence;

    @Lob
    @Column(name = "correctSentence", nullable = false, length = Integer.MAX_VALUE)
    private String correctSentence;

    @Column(name = "content", nullable = false)
    private String content;


    public StringComplete() {

    }


    public StringComplete(String contentName, int contentPosition, Type content_category, Modules module, String enunciado, String sentence, String correctSentence, String content) {
        super(contentName, contentPosition, content_category, module);
        this.enunciado = enunciado;
        this.sentence = sentence;
        this.correctSentence = correctSentence;
        this.content = content;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getCorrectSentence() {
        return correctSentence;
    }

    public void setCorrectSentence(String correctSentence) {
        this.correctSentence = correctSentence;
    }


    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringComplete other = (StringComplete) obj;
        if (getContentId() == null) {
            if (other.getContentId() != null)
                return false;
        } else if (!getContentId().equals(other.getContentId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getContentId() == null) ? 0 : getContentId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "StringComplete{" +
                "enunciado='" + enunciado + '\'' +
                ", sentence='" + sentence + '\'' +
                ", correctSentence='" + correctSentence + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
