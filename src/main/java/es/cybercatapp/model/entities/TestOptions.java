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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contentId", nullable = false)
    private Content content;


    public TestOptions(){}

    public TestOptions(String stringOption, boolean correct) {
        this.stringOption = stringOption;
        this.correct = correct;
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
}

