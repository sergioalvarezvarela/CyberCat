package es.cybercatapp.web.conversor;

import es.cybercatapp.model.entities.Comment;
import es.cybercatapp.web.dto.CommentDtoForm;
import es.cybercatapp.web.utils.ImageUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommentConversor {

    private static String toFechaFormatter (LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        return date.format(formatter);
    }
    public static CommentDtoForm toCommentDtoForm(String username , Comment comment, byte[] image, String path) {
        return (CommentDtoForm) new CommentDtoForm(username, ImageUtils.toString64(image), path, toFechaFormatter(comment.getCreation_date()), comment.getDescription(), comment.getGrade(), comment.getCommentary(), comment.getCommentId());
    }
}
