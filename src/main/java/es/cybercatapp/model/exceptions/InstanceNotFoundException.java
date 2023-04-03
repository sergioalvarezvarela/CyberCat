package es.cybercatapp.model.exceptions;


public class InstanceNotFoundException extends Exception {
    private static final long serialVersionUID = -3927315439648732390L;

    private final String id;
    private final String type;

    public InstanceNotFoundException(String id, String type, String message) {
        super(message);
        this.id = id;
        this.type = type;
    }

    public Object getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
