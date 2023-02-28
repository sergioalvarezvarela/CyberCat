package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.dto.LoginDtoForm;
import es.cybercatapp.service.dto.RegisterDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Locale;


@Controller
public class UserOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;


    @Autowired
    private UserImpl userImpl;

    @GetMapping(value = {"/register"})
    public String doGetRegisterPage(Model model) {
        model.addAttribute("RegisterDtoForm", new RegisterDtoForm());
        return "register";
    }

    @PostMapping(value = {"/register"})
    public String doRegister(@Valid @ModelAttribute("RegisterDtoForm") RegisterDtoForm registerDtoForm,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             HttpSession session,
                             Locale locale,
                             Model model) {
        if (result.hasErrors()) {
            serviceExceptions.serviceInvalidFormError(result,
                    "registration.invalid.parameters", model, locale);
            return "register";
        }
        Users user;
        try {
            user = userImpl.create(registerDtoForm.getUsername(), registerDtoForm.getEmail(),
                    registerDtoForm.getPassword(),
                    registerDtoForm.getImage() != null ? registerDtoForm.getImage().getOriginalFilename() : null,
                    registerDtoForm.getImage() != null ? registerDtoForm.getImage().getBytes() : null);
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("User {0} with username {1} registered", user.getEmail(), user.getUsername()));
            }
            session.setAttribute(Constants.USER_SESSION, user);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "registration.success", new Object[]{user.getUsername()}, locale));
        } catch (DuplicatedResourceException ex) {
            serviceExceptions.serviceDuplicatedResourceException(ex , model);
            return "register";
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + Constants.ROOT_ENDPOINT;
    }

    @GetMapping("/")
    public String doGetLogin(Model model) {
            model.addAttribute("LoginDtoForm", new LoginDtoForm());
            return "Index"; //
    }

    @PostMapping("/")
    public String login(@Valid @ModelAttribute("LoginDtoForm") LoginDtoForm loginDtoForm, BindingResult result,
                        @RequestParam(value = "next", required = false) String next,
                        HttpSession session,
                        HttpServletResponse response,
                        Locale locale,
                        Model model) {
        if (result.hasErrors()) {
            serviceExceptions.serviceInvalidFormError(result,
                    "login.invalid.parameters", model, locale);
            return "Index";
        }
        Users user;
        try {
            user = userImpl.login(loginDtoForm.getEmail(), loginDtoForm.getPassword());
            session.setAttribute(Constants.USER_SESSION, user);
            if(logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("El usuario {0} se ha logueado", user.getEmail()));
            }
            if (loginDtoForm.getRememberMe() != null && loginDtoForm.getRememberMe()) {

            }
        } catch (AuthenticationException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("User {0} not logged in ", loginDtoForm.getEmail()));
            }
            return serviceExceptions.serviceAuthenticationException(ex, loginDtoForm.getEmail(),
                    "Index", model, locale);
        }
        if (next != null && next.trim().length() > 0) {
            return Constants.SEND_REDIRECT + next;
        }
        return Constants.SEND_REDIRECT + "/profile";
    }

}

