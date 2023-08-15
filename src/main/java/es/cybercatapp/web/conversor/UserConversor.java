package es.cybercatapp.web.conversor;

import es.cybercatapp.model.entities.Users;
import es.cybercatapp.web.dto.ProfileDtoForm;
import es.cybercatapp.web.utils.ImageUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserConversor {

    public static String formatDateTime(String inputDateTime, String outputFormat) {
        LocalDate dateTime = LocalDate.parse(inputDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);

        return dateTime.format(formatter);
    }

    public static ProfileDtoForm toProfile(Users users, byte[] image, String path) {
        return (ProfileDtoForm) new ProfileDtoForm(users.getUsername(), users.getEmail(), formatDateTime(users.getFecha_creacion().toString(), "dd/MM/yyyy"), ImageUtils.toString64(image), path);
    }

}

