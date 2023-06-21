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
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@Component
public class ServiceExceptions {

    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptions.class);

    @Autowired
    private MessageSource messageSource;


    public String serviceInstanceNotFoundException(InstanceNotFoundException e, Model model, Locale locale) {
        logger.error(e.getMessage(), e);
        model.addAttribute(Constants.ERROR_MESSAGE, e.getMessage());
        model.addAttribute(Constants.EXCEPTION, e);
        return Constants.ERROR_PAGE;
    }

    public String serviceUnexpectedException(Exception e, Model model) {
        logger.error(e.getMessage(), e);
        model.addAttribute(Constants.ERROR_MESSAGE, e.getMessage());
        model.addAttribute(Constants.EXCEPTION, e);
        return Constants.ERROR_PAGE;
    }

    public String serviceUsernameNotFoundException(Exception e, Model model) {
        logger.error(e.getMessage(), e);
        model.addAttribute(Constants.ERROR_MESSAGE, e.getMessage());
        model.addAttribute(Constants.EXCEPTION, e);
        return Constants.ERROR_PAGE;
    }

}
