package es.cybercatapp.model.entities;

public enum Type {
    TEORIC("teoric"),
    TESTCHOOSE("select"),

    TESTPUZZLE("puzzle");

    private final String descripcion;

    Type(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
