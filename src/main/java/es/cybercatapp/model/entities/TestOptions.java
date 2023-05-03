package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;

@Entity(name = Constants.TESTOPTIONS_ENTITY)
@Table(name = Constants.TESTOPTIONS_TABLE)
public class TestOptions  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long testOptionId;


    @Column(name="stringOption",nullable = false)
    private String stringOption;

    @Column(name="correct",nullable = false)
    private Boolean correct;
    @ManyToOne(cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "contentId", nullable = false)
    private Content content;


    public TestOptions(){}

    public TestOptions(String stringOption, Boolean correct, Content content) {
        this.stringOption = stringOption;
        this.correct = correct;
        this.content = content;
    }

    public Long getTestOptionId() {
        return testOptionId;
    }

    public void setTestOptionId(Long testOptionId) {
        this.testOptionId = testOptionId;
    }

    public String getStringOption() {
        return stringOption;
    }

    public void setStringOption(String stringOption) {
        this.stringOption = stringOption;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestOptions other = (TestOptions) obj;
        if (getTestOptionId() == null) {
            if (other.getTestOptionId() != null)
                return false;
        } else if (!getTestOptionId().equals(other.getTestOptionId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTestOptionId() == null) ? 0 : getTestOptionId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TestOptions{" +
                "testOptionId=" + testOptionId +
                ", stringOption='" + stringOption + '\'' +
                ", correct=" + correct +
                '}';
    }
}

