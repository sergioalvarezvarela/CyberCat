package es.cybercatapp.web.dto;

public class DiplomaDtoForm {

    boolean courseCompleted;

    long diplomaId;

    boolean paymentCompleted;

    float price;

    public DiplomaDtoForm(boolean courseCompleted, long diplomaId, boolean paymentCompleted, float price) {
        this.courseCompleted = courseCompleted;
        this.diplomaId = diplomaId;
        this.paymentCompleted = paymentCompleted;
        this.price = price;
    }

    public DiplomaDtoForm(boolean courseCompleted, float price, boolean paymentCompleted) {
        this.courseCompleted = courseCompleted;
        this.price = price;
        this.paymentCompleted = paymentCompleted;
    }

    public DiplomaDtoForm() {
    }

    public boolean isCourseCompleted() {
        return courseCompleted;
    }

    public void setCourseCompleted(boolean courseCompleted) {
        this.courseCompleted = courseCompleted;
    }

    public long getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(long diplomaId) {
        this.diplomaId = diplomaId;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
