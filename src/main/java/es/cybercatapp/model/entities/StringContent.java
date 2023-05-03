package es.cybercatapp.model.entities;

import es.cybercatapp.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = Constants.TESTSTRING_TABLE)
@Table(name = Constants.TESTSTRING_ENTITY)
public class StringContent extends Content {
    private static final long serialVersionUID = -2396193026881035672L;
    @Lob
    @Column(name = "html", nullable = false, length = Integer.MAX_VALUE)
    private String html;


    public StringContent() {

    }

    public StringContent(String contentName, int contentPosition, Type content_category, Modules module, String html) {
        super(contentName, contentPosition, content_category, module);
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringContent other = (StringContent) obj;
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
        return "StringContent{" +
                "html='" + html + '\'' +
                '}';
    }
}
