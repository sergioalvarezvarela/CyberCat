package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.conversor.UserConversor;
import es.cybercatapp.service.dto.EditProfileDtoForm;
import es.cybercatapp.service.dto.LoginDtoForm;
import es.cybercatapp.service.dto.ProfileDtoForm;
import es.cybercatapp.service.dto.RegisterDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
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
            serviceExceptions.serviceDuplicatedResourceException(ex, model);
            return "register";
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + Constants.ROOT_ENDPOINT;
    }

    @GetMapping("/")
    public String doGetIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("LoginDtoForm", new LoginDtoForm());
            return "Index";
        } else {
            return "redirect:/profile/" + authentication.getName();
        }

    }

    @GetMapping("/profile/{username}")
    public String doGetProfile(@PathVariable String username, Model model, Principal principal, Locale locale) throws IOException, InstanceNotFoundException {
        Users user = userImpl.findByUsername(username);
        byte[] image = userImpl.getImage(user.getUserId());
        ProfileDtoForm profileDtoForm = UserConversor.toProfile(user, image);
        model.addAttribute("ProfileDtoForm", profileDtoForm);
        return "profile";
    }

    @GetMapping("/{username}/editprofile")
    public String doGetEditProfile(@PathVariable String username, Model model, Principal principal, Locale locale) throws IOException {
        model.addAttribute("EditProfileDtoForm", new  EditProfileDtoForm());
        return "editprofile";
    }

    @PostMapping("/profile/editprofile/remove")
    public String doPostRemoveProfile(Principal principal, Locale locale,
                                      RedirectAttributes redirectAttributes) {



        userImpl.Remove(principal.getName());
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removeprofile.success", new Object[]{principal.getName()}, locale));
        return Constants.SEND_REDIRECT + "/logout";
    }

    @PostMapping("/profile/editprofile/changepassword")
    public String doPostChangePassword(Principal principal, @Valid @ModelAttribute("EditProfileDtoForm") EditProfileDtoForm editProfileDtoForm,
                                       RedirectAttributes redirectAttributes,
                                       Locale locale,
                                       Model model) {
        principal.getName();

        try {
            userImpl.changePassword(principal.getName(),editProfileDtoForm.getOldPass(),editProfileDtoForm.getNewPassword() );
        } catch (AuthenticationException e) {
            serviceExceptions.serviceAuthenticationException(e,model);
            return "editprofile";
        }

        model.addAttribute(Constants.SUCCESS_MESSAGE,messageSource.getMessage(
                "changepassword.success", new Object[]{principal.getName()}, locale));
        return "editprofile";
    }
}




