package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.CommentImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.CommentDtoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

@Controller
public class CommentOperations {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @Autowired
    private CommentImpl commentImpl;


    @PostMapping("/course/{id}/addcomment")
    public String doPostAddComment(@PathVariable String id, @Valid @ModelAttribute("CommentDtoForm") CommentDtoForm commentDtoForm,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes, Locale locale, Model model, Principal principal) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "addcomment.invalid.parameters", id, locale, redirectAttributes);
            return Constants.SEND_REDIRECT + "/course/" + id;
        }
        try {
            commentImpl.create(Long.parseLong(id), principal.getName(), commentDtoForm.getDescription(), commentDtoForm.getGrade(), commentDtoForm.getCommentary());
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
}
