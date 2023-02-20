package es.cybercatapp.model.utils;

import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionGenerationUtils {

    @Autowired
    private MessageSource messageSource;

    public InstanceNotFoundException toInstanceNotFoundException(Long id, String type, String messageKey)
            throws InstanceNotFoundException {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageKey, new Object[]{type, id}, locale);
        return new InstanceNotFoundException(id, type, message);
    }

    public DuplicatedResourceException toDuplicatedResourceException(String resource, String value, String messageKey)
            throws DuplicatedResourceException {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageKey, new Object[]{value, resource}, locale);
        return new DuplicatedResourceException(resource, value, message);
    }
}
