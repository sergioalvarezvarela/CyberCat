package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Diploma;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.*;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.CourseDtoForm;
import es.cybercatapp.service.dto.DiplomaDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Locale;

@Controller
public class DiplomaOperations {
    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    private DiplomaImpl diplomaImpl;


    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @PostMapping(value = {"/course/{courseId}/createDiploma"})
    public String doPostAddDiploma(@PathVariable String courseId,
                                   Principal principal, Locale locale, Model model) {
        Diploma diploma = diplomaImpl.findDiplomaByCourseAndUsername(Long.parseLong(courseId), principal.getName());;
        try {
            diploma = diplomaImpl.create(Long.valueOf(courseId), principal.getName());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Diploma con id {0} creado", diploma.getDiplomaId()));
            }
            diploma = diplomaImpl.findDiplomaByCourseAndUsername(Long.parseLong(courseId), principal.getName());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException e) {
            return Constants.SEND_REDIRECT + "/diploma/" + diploma.getDiplomaId().toString();
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + "/diploma/" + diploma.getDiplomaId().toString();
    }

    @GetMapping(value = {"/diploma/{diplomaId}"})
    public void doPostGetDiploma(@PathVariable String diplomaId,
                                 HttpServletResponse response) {
        Diploma diploma;
        try {
            diploma = diplomaImpl.findDiplomaById(Long.parseLong(diplomaId));
            byte[] image = diplomaImpl.getPdf(diploma.getDiplomaId());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Diploma con id {0} encontrado", diploma.getDiplomaId()));
            }
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename" + diploma.getCourses().getCourseId() + "_"+ diploma.getUsers().getUsername());
            response.setHeader("Content-Length", String.valueOf(image.length));
            response.getOutputStream().write(image);
        } catch (InstanceNotFoundException ex) {


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
