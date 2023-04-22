package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*
@Entity(name = Constants.TESTQUESTIONS_TABLE)
@Table(name = Constants.TESTQUESTIONS_ENTITY)
public class TestQuestions extends Content {
    @Column(name="question",nullable = false)
    private String question;

    @OneToMany(
            mappedBy = "content",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TestOptions> testOptions = new ArrayList<>();

    public TestQuestions(){

    }

    public TestQuestions(String question) {
        this.question = question;
    }

    public TestQuestions(String contentName, Module module, String question) {
        super(contentName, module);
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<TestOptions> getTestOptions() {
        return testOptions;
    }

    public void setTestOptions(List<TestOptions> testOptions) {
        this.testOptions = testOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TestQuestions that = (TestQuestions) o;
        return Objects.equals(question, that.question) && Objects.equals(testOptions, that.testOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), question, testOptions);
    }


}*/
