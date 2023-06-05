package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = Constants.TESTQUESTIONS_TABLE)
@Table(name = Constants.TESTQUESTIONS_ENTITY)
public class TestQuestions extends Content {
    private static final long serialVersionUID = -6410842115245824837L;
    @Column(name="question",nullable = false)
    private String question;


    @Column(name="option1",nullable = false)
    private String option1;

    @Column(name="option2",nullable = false)
    private String option2;

    @Column(name="option3",nullable = false)
    private String option3;

    @Column(name="option4",nullable = false)
    private String option4;

    @Column(name="correct",nullable = false)
    private int correct;

    public TestQuestions(){

    }

    public TestQuestions(String contentName, int contentPosition, Type content_category, Modules module, String question, String option1, String option2, String option3, String option4, int correct) {
        super(contentName, contentPosition, content_category, module);
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
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
        TestQuestions other = (TestQuestions) obj;
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
        return "TestQuestions{" +
                "question='" + question + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", correct=" + correct +
                '}';
    }
}