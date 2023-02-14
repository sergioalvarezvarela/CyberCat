package es.cybercatapp.business.exceptions;


public class InstanceNotFoundException extends Exception {
    private static final long serialVersionUID = -3927315439648732390L;

    private final Long id;
    private final String type;

    public InstanceNotFoundException(Long id, String type, String message) {
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
