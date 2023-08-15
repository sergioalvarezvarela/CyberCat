package es.cybercatapp.web.dto;

public class DiplomaProfileDtoForm {
    long diplomaId;

    private String image;

    private String imageType;

    public DiplomaProfileDtoForm(long diplomaId, String image, String imageType) {
        this.diplomaId = diplomaId;
        this.image = image;
        this.imageType = imageType;
    }

    public long getDiplomaId() {
        return diplomaId;
    }

    public void setDiplomaId(long diplomaId) {
        this.diplomaId = diplomaId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
