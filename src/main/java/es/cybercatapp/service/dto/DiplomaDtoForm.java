package es.cybercatapp.service.dto;

public class DiplomaDtoForm {

    boolean courseCompleted;

    byte[] pdf;

    long diplomaId;

    public DiplomaDtoForm(boolean courseCompleted, long diplomaId) {
        this.courseCompleted = courseCompleted;
        this.diplomaId = diplomaId;
    }
    public DiplomaDtoForm(boolean courseCompleted, byte[] pdf, long diplomaId) {
        this.courseCompleted = courseCompleted;
        this.pdf = pdf;
        this.diplomaId = diplomaId;
    }

    public DiplomaDtoForm(boolean courseCompleted) {
        this.courseCompleted = courseCompleted;
    }


    public boolean isCourseCompleted() {
        return courseCompleted;
    }

    public void setCourseCompleted(boolean courseCompleted) {
        this.courseCompleted = courseCompleted;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public long getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(long diplomaId) {
        this.diplomaId = diplomaId;
    }
}
