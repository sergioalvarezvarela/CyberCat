package es.cybercatapp.common;

public class Constants {

    private Constants() {
    }
    // User
    public static final String USER_ENTITY = "User";
    public static final String USER_TABLE = "Users";

    public static final String EMAIL_FIELD = "email";
    public static final String USERNAME_FIELD = "username";

    // Exceptions
    public static final String EXCEPTION = "exception";
    public static final String DUPLICATED_INSTANCE_MESSAGE = "duplicated.instance.exception";
    public static final String INSTANCE_NOT_FOUND_MESSAGE = "instance.not.found.exception";

    // Operations
    public static final String REGISTRATION_ENDPOINT = "/registration";

    // Forms
    public static final String USER_PROFILE_FORM = "UserDtoForm";

    // Error
    public static final String ERROR_PAGE = "error";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String REGISTRATION_INVALID_PARAMS_MESSAGE = "registration.invalid.parameters";

    //success
    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String REGISTRATION_SUCCESS_MESSAGE = "registration.success";
    //paginas
    public static final String USER_PROFILE_PAGE = "Profile";

    public static final String SEND_REDIRECT = "redirect:";
    public static final String ROOT_ENDPOINT = "/";
    public static final String USER_SESSION = "user";


}
