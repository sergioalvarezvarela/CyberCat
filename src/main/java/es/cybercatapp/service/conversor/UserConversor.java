package es.cybercatapp.service.conversor;

import es.cybercatapp.model.entities.Users;
import es.cybercatapp.service.dto.ProfileDtoForm;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserConversor {

    public static String formatDateTime(String inputDateTime, String outputFormat) {
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);

        return dateTime.format(formatter);
    }

    public static ProfileDtoForm toProfile(Users users, byte[] image) throws IOException {
        return (ProfileDtoForm) new ProfileDtoForm(users.getUsername(), users.getEmail(), formatDateTime(users.getFecha_creacion().toString(), "dd/MM/yyyy"), image);
    }
}
