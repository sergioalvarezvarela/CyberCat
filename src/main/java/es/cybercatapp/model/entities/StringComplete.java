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

    @OneToMany(
            mappedBy = "stringComplete",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}
    )
    private List<StringCompleteOptions> stringCompleteOptions=  new ArrayList<>();

    public StringComplete() {

    }


    public StringComplete(String contentName, int contentPosition, Type content_category, Modules module, String enunciado, String sentence, String correctSentence) {
        super(contentName, contentPosition, content_category, module);
        this.enunciado = enunciado;
        this.sentence = sentence;
        this.correctSentence = correctSentence;
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

    public List<StringCompleteOptions> getStringCompleteOptions() {
        return stringCompleteOptions;
    }

    public void setStringCompleteOptions(List<StringCompleteOptions> stringCompleteOptions) {
        this.stringCompleteOptions = stringCompleteOptions;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
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
                '}';
    }
}
