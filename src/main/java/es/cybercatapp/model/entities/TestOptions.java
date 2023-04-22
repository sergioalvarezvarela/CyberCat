package es.cybercatapp.model.entities;
/*
import es.cybercatapp.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = Constants.TESTOPTIONS_ENTITY)
@Table(name = Constants.TESTOPTIONS_TABLE)
public class TestOptions  {

    @EmbeddedId
    private TestOptionsId id;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "contentName", referencedColumnName = "contentName", insertable = false, updatable = false),
            @JoinColumn(name = "moduleName", referencedColumnName = "moduleName", insertable = false, updatable = false),
            @JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)
    })
    private Content content;


    public TestOptions(){}

    public TestOptions(String optionString, Content content) {
        this.content = content;
        this.id = new TestOptionsId(optionString, content.getContentId());
    }

    public TestOptionsId getId() {
        return id;
    }

    public void setId(TestOptionsId id) {
        this.id = id;
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
        return id.equals(that.id) && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public String toString() {
        return "TestOptions{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}*/
