package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.conversor.UserConversor;
import es.cybercatapp.service.dto.*;
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
import org.springframework.web.bind.annotation.*;
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
    public String doGetProfile(@PathVariable String username, Model model) throws IOException, InstanceNotFoundException {
        model.addAttribute("username", username);
        Users user = userImpl.findByUsername(username);
        byte[] image = userImpl.getImage(user.getUserId());
        ProfileDtoForm profileDtoForm = UserConversor.toProfile(user, image);
        model.addAttribute("ProfileDtoForm", profileDtoForm);
        return "profile";
    }

    @GetMapping("/editprofile")
    public String doGetEditProfile(Model model, Principal principal) {
        System.out.println(principal);
        System.out.println("hola");
        Users user = userImpl.findByUsername(principal.getName());
        model.addAttribute("EditProfileDtoForm", new EditProfileDtoForm(user.getUsername(), user.getEmail()));
        model.addAttribute("ChangePasswordDtoForm", new ChangePasswordDtoForm());
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
    public String doPostChangePassword(Principal principal, @Valid @ModelAttribute("ChangePasswordDtoForm") ChangePasswordDtoForm changePasswordDtoForm,
                                       Locale locale, BindingResult result, HttpSession session,
                                       Model model) {
        Users user = userImpl.findByUsername(principal.getName());
        model.addAttribute("EditProfileDtoForm", new EditProfileDtoForm(user.getUsername(), user.getEmail()));

        if (result.hasErrors()) {
            serviceExceptions.serviceInvalidFormError(result,
                    "changepassword.invalid.parameters", model, locale);
            return "editprofile";
        }

        try {
            userImpl.changePassword(principal.getName(), changePasswordDtoForm.getOldPass(), changePasswordDtoForm.getNewPassword());
        } catch (AuthenticationException e) {
            serviceExceptions.serviceAuthenticationException(e, model);
            return "editprofile";
        }
        model.addAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "changepassword.success", new Object[]{principal.getName()}, locale));
        return "editprofile";
    }

    @PostMapping("/profile/editprofile/modify")
    public String doPostModifyProfile(Principal principal, @Valid @ModelAttribute("EditProfileDtoForm") EditProfileDtoForm editProfileDtoForm,
                                      Locale locale, BindingResult result,
                                      Model model) {
        model.addAttribute("ChangePasswordDtoForm", new ChangePasswordDtoForm());
        if (result.hasErrors()) {
            serviceExceptions.serviceInvalidFormError(result,
                    "modifyprofile.invalid.parameters", model, locale);
            return "editprofile";
        }

        try {
            userImpl.modifyProfile(principal.getName(), editProfileDtoForm.getUsername(), editProfileDtoForm.getEmail());
        } catch (DuplicatedResourceException ex) {
            serviceExceptions.serviceDuplicatedResourceException(ex, model);
            return "editprofile";
        }

        model.addAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "modifyprofile.success", new Object[]{principal.getName()}, locale));
        return "editprofile";
    }


}




