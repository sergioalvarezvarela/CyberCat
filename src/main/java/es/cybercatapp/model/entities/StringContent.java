package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = Constants.TESTSTRING_TABLE)
@Table(name = Constants.TESTSTRING_ENTITY)
public class StringContent extends Content {
    @Lob
    @Column(name = "html", nullable = false, length = Integer.MAX_VALUE)
    private String html;


    public StringContent() {

    }

    public StringContent(String contentName, int contentPosition, Modules module, String html) {
        super(contentName, contentPosition, module);
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
