package es.cybercatapp.service.Exceptions;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Component
public class ServiceRedirectExceptions {
    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptions.class);

    @Autowired
    private MessageSource messageSource;

    public void serviceInvalidFormError(BindingResult result, String message, String name,
                                        Locale locale, RedirectAttributes redirectAttributes) {
        if (logger.isErrorEnabled()) {
            logger.error(result.toString());
        }
        redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, messageSource.getMessage(
                message, new Object[]{name}, locale));
    }

    public void serviceDuplicatedResourceException(DuplicatedResourceException e ,
                                                   RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage(),e);
        redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE,e.getMessage());
    }

    public void serviceAuthenticationException(AuthenticationException e,
                                               RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage(),e);
        redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE,e.getMessage());
    }
}
