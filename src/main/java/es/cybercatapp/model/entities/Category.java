package es.cybercatapp.model.entities;

public enum Category {
    VIRUS("Virus"),
    MALWARE("Malware"),
    CRIPTOGRAFIA("Criptografia"),
    FORENSE("Forense"),
    RED("Red"),
    INFRAESTRUCTURA("Infraestructura"),
    NUBE("Nube"),
    OTRO("Otro");

    private final String descripcion;

    Category(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
