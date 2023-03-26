package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Constants.TESTOPTIONS_ENTITY)
@Table(name = Constants.TESTOPTIONS_TABLE)
public class TestOptions {
    @EmbeddedId
    private TestOptionsId id;
    @Column(name= "optionString", nullable = false)
    private String optionString;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "contentId", referencedColumnName = "contentId", insertable = false, updatable = false),
            @JoinColumn(name = "moduleId", referencedColumnName = "moduleId", insertable = false, updatable = false),
            @JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)
    })
    private Content content;

    public TestOptions(){}

    public TestOptions(String optionString, Content content) {
        this.optionString = optionString;
        this.content = content;
        this.id = new TestOptionsId(null,content.getId().getContentId(), content.getId().getModuleId(), content.getId().getCourseId());
    }

    public TestOptionsId getId() {
        return id;
    }

    public void setId(TestOptionsId id) {
        this.id = id;
    }

    public String getOptionString() {
        return optionString;
    }

    public void setOptionString(String optionString) {
        this.optionString = optionString;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestOptions that = (TestOptions) o;
        return Objects.equals(id, that.id) && Objects.equals(optionString, that.optionString) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, optionString, content);
    }

    @Override
    public String toString() {
        return "TestOptions{" +
                "id=" + id +
                ", optionString='" + optionString + '\'' +
                ", content=" + content +
                '}';
    }
}
