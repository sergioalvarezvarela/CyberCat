package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Comment;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.CommentImpl;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.conversor.CommentConversor;
import es.cybercatapp.service.dto.CommentDtoForm;
import es.cybercatapp.service.dto.LoginDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class CommentOperations {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @Autowired
    private CommentImpl commentImpl;

    @Autowired
    private UserImpl userImpl;


    @PostMapping("/course/{id}/addcomment")
    public String doPostAddComment(@PathVariable String id, @Valid @ModelAttribute("CommentDtoForm") CommentDtoForm commentDtoForm,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes, Locale locale, Model model, Principal principal) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "addcomment.invalid.parameters", id, locale, redirectAttributes);
            return Constants.SEND_REDIRECT + "/course/" + id;
        }
        Comment comment;
        try {
           comment = commentImpl.create(Long.parseLong(id), principal.getName(), commentDtoForm.getDescription(), commentDtoForm.getGrade(), commentDtoForm.getCommentary());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Comentario con id {1} creado", comment.getCommentId()));
            }
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addcomment.success", new Object[]{id}, locale));
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return Constants.SEND_REDIRECT + "/course/" + id;
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

        return Constants.SEND_REDIRECT + "/course/" + id;
    }

    @PostMapping("/course/{courseId}/removecomment/{commentId}")
    public String doPostDeleteComment(@PathVariable String courseId, @PathVariable String commentId,
                                      RedirectAttributes redirectAttributes, Locale locale, Model model) {
        try {
            commentImpl.remove(Long.parseLong(commentId));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Comentario con id {1} eliminado", commentId));
            }
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "removecomment.success", new Object[]{commentId}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

        return Constants.SEND_REDIRECT + "/course/" + courseId;
    }

    @PostMapping("/course/{courseId}/editcomment/{commentId}")
    public String doPostEditComment(@PathVariable String courseId, @PathVariable String commentId, @Valid @ModelAttribute("CommentDtoForm") CommentDtoForm commentDtoForm,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes, Locale locale, Model model) {
        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "editcomment.invalid.parameters", commentId, locale, redirectAttributes);
            return Constants.SEND_REDIRECT + "/course/" + courseId;
        }
        try {
            commentImpl.update(Long.parseLong(commentId), commentDtoForm.getDescription(), commentDtoForm.getGrade(), commentDtoForm.getCommentary());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Comentario con id {1} editado", commentId));
            }
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "editcomment.success", new Object[]{commentId}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return Constants.SEND_REDIRECT + "/course/" + courseId;
        }

        return Constants.SEND_REDIRECT + "/course/" + courseId;
    }

    @GetMapping("/course/{courseId}/seecomments")
    public String doPostGetComments(@PathVariable String courseId, @RequestParam("start") String start,
                                    Locale locale,
                                    Model model, Principal principal) {
        try {
            List<Comment> comments = commentImpl.findCommentsbyCourse(Long.parseLong(courseId), Integer.parseInt(start));
            List<CommentDtoForm> commentDto = new ArrayList<>();

            for (Comment comment : comments) {
                byte[] image = userImpl.getImage(comment.getUsers().getUserId());
                commentDto.add(CommentConversor.toCommentDtoForm(comment.getUsers().getUsername(), comment, image, comment.getUsers().getImagen_perfil()));
            }
            model.addAttribute("CommentDtoForm", commentDto);
            model.addAttribute("courseId", courseId);
            model.addAttribute("principal", principal);
            model.addAttribute("start",Integer.valueOf(start));
            model.addAttribute("LoginDtoForm", new LoginDtoForm());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);


        }
        return "seecomments";
    }
}
