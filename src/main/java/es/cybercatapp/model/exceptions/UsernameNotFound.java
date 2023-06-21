package es.cybercatapp.model.exceptions;

public class UsernameNotFound extends Exception {

    private final String username;

    public UsernameNotFound(String username, String message) {
        super(message);
        this.username = username;
    }

    public Object getId() {
        return username;
    }

}
