package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = Constants.STRINGCOMPLETEOPTIONS_ENTITY)
@Table(name = Constants.STRINGCOMPLETEOPTIONS_TABLE)
public class StringCompleteOptions implements Serializable {
    private static final long serialVersionUID = -8987856253677035931L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long stringCompleteId;

    @Column(name="stringCompleteOption",nullable = false)
    private String stringCompleteOption;

    @ManyToOne(cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id", nullable = false)
    private StringComplete stringComplete;


    public StringCompleteOptions(String stringCompleteOption, StringComplete stringComplete) {
        this.stringCompleteOption = stringCompleteOption;
        this.stringComplete = stringComplete;
    }

    public StringCompleteOptions() {

    }

    public Long getStringCompleteId() {
        return stringCompleteId;
    }

    public void setStringCompleteId(Long stringCompleteId) {
        this.stringCompleteId = stringCompleteId;
    }

    public String getStringCompleteOption() {
        return stringCompleteOption;
    }

    public void setStringCompleteOption(String stringCompleteOption) {
        this.stringCompleteOption = stringCompleteOption;
    }

    public Content getStringComplete() {
        return stringComplete;
    }

    public void setStringComplete(StringComplete stringComplete) {
        this.stringComplete = stringComplete;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stringCompleteId == null) ? 0 : stringCompleteId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringCompleteOptions other = (StringCompleteOptions) obj;
        if (stringCompleteId == null) {
            if (other.stringCompleteId != null)
                return false;
        } else if (!stringCompleteId.equals(other.stringCompleteId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StringCompleteOptions{" +
                "stringCompleteId=" + stringCompleteId +
                ", stringCompleteOption='" + stringCompleteOption + '\'' +
                '}';
    }
}
