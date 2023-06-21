package es.cybercatapp.service.Controllers;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.ProfilePhoto;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.InscriptionsImpl;
import es.cybercatapp.service.conversor.CoursesConversor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.utils.AdminChecker;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.conversor.UserConversor;
import es.cybercatapp.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;


@Controller
public class UserOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;


    @Autowired
    private UserImpl userImpl;

    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private InscriptionsImpl inscriptionsImpl;




    @GetMapping(value = {"/register"})
    public String doGetRegisterPage(Model model) {
        model.addAttribute("LoginDtoForm", new LoginDtoForm());
        model.addAttribute("RegisterDtoForm", new RegisterDtoForm());
        return "register";
    }

    @PostMapping(value = {"/register"})
    public String doRegister(@Valid @ModelAttribute("RegisterDtoForm") RegisterDtoForm registerDtoForm,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             HttpSession session, Principal principal,
                             Locale locale,
                             Model model) {
        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result,
                    "registration.invalid.parameters", principal.getName(), locale,redirectAttributes);
            return "redirect:/register";
        }
        Users user;
        try {
            ProfilePhoto profilePhoto = new ProfilePhoto();
            ProfilePhoto.PhotoData randomPhoto = profilePhoto.getRandomPhoto();
            user = userImpl.create(registerDtoForm.getUsername(), registerDtoForm.getEmail(),
                    registerDtoForm.getPassword(),
                    !registerDtoForm.getImage().isEmpty() ? registerDtoForm.getImage().getOriginalFilename() : randomPhoto.getName(),
                    !registerDtoForm.getImage().isEmpty() ? registerDtoForm.getImage().getBytes() : randomPhoto.getBytes());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Usuario {0} con nombre de usuario {1} registrado", user.getEmail(), user.getUsername()));
            }
            session.setAttribute(Constants.USER_SESSION, user);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "registration.success", new Object[]{user.getUsername()}, locale));
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex,redirectAttributes);
            return "redirect:/register";
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + Constants.ROOT_ENDPOINT;
    }

    @GetMapping("/")
    public String doGetIndex(Model model, Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("LoginDtoForm", new LoginDtoForm());
            List<Courses> courses = courseImpl.findCoursesMoreInscriptions();
            List<CourseDtoForm> courseDto = new ArrayList<>();
            for (Courses course : courses) {
                try {
                    byte[] image2 = courseImpl.getImage(course.getCourseId());
                    courseDto.add(CoursesConversor.toCourseDtoForm(course, image2, course.getCourse_photo()));
                    model.addAttribute("CourseDtoForm", courseDto);
                }catch (InstanceNotFoundException ex){
                    return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
                }
            }
            return "Index";
        } else {
            return "redirect:/profile/" + authentication.getName();
        }

    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String doGetProfile(@PathVariable String username, Model model, Authentication authentication) {
        try {
            model.addAttribute("username", username);
            Users user = userImpl.findByUsername(username);
            byte[] image = userImpl.getImage(user.getUserId());
            ProfileDtoForm profileDtoForm = UserConversor.toProfile(user, image, user.getImagen_perfil());
            model.addAttribute("ProfileDtoForm", profileDtoForm);
            AdminChecker.isAdmin(model, authentication);
            List<Courses> courses = inscriptionsImpl.findCoursesInscriptionsByUser(authentication.getName());
            List<CourseDtoForm> courseDto = new ArrayList<>();

            for (Courses course : courses) {
                byte[] image2 = courseImpl.getImage(course.getCourseId());
                courseDto.add(CoursesConversor.toCourseDtoForm(course, image2, course.getCourse_photo()));
            }
            model.addAttribute("CourseDtoForm", courseDto);
            return "profile";
        } catch (Exception ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }

    }




    @GetMapping("/profile/{username}/editprofile")
    @PreAuthorize("#username == authentication.principal.username")
    public String doGetEditProfile(@PathVariable String username, Model model, Principal principal, Locale locale) {
        try {
            Users user = userImpl.findByUsername(principal.getName());
            byte[] image = userImpl.getImage(user.getUserId());
            ProfileDtoForm editProfileDtoForm = UserConversor.toProfile(user, image, user.getImagen_perfil());
            model.addAttribute("EditProfileDtoForm", editProfileDtoForm);
            model.addAttribute("ChangePasswordDtoForm", new ChangePasswordDtoForm());
            model.addAttribute("UpdateImageProfileDtoForm", new UpdateImageProfileDtoForm());
            return "editprofile";
        } catch (InstanceNotFoundException ex) {
           return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

    }

    @PostMapping("/editprofile/remove/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String doPostRemoveProfile(@PathVariable String username, Principal principal, Locale locale,
                                      RedirectAttributes redirectAttributes, Model model) {

        try{
            userImpl.Remove(principal.getName());

        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removeprofile.success", new Object[]{principal.getName()}, locale));
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("Perfil de usuario {0} elimiado", principal.getName()));
        }
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        } catch (UsernameNotFound ex) {
            return serviceExceptions.serviceUsernameNotFoundException(ex, model);
        }
        return Constants.SEND_REDIRECT + "/logout";
    }

    @PostMapping("/editprofile/changepassword")
    public String doPostChangePassword(Principal principal, @Valid @ModelAttribute("ChangePasswordDtoForm") ChangePasswordDtoForm changePasswordDtoForm,
                                        BindingResult result, Locale locale,
                                       Model model, RedirectAttributes redirectAttributes) {
        try {
            Users user = userImpl.findByUsername(principal.getName());
            byte[] image = userImpl.getImage(user.getUserId());
            ProfileDtoForm editProfileDtoForm = UserConversor.toProfile(user, image, user.getImagen_perfil());
            model.addAttribute("EditProfileDtoForm", editProfileDtoForm);
            model.addAttribute("UpdateImageProfileDtoForm", new UpdateImageProfileDtoForm());

            if (result.hasErrors()) {
                serviceRedirectExceptions.serviceInvalidFormError(result,
                        "changepassword.invalid.parameters",principal.getName(),locale,redirectAttributes);
                return "redirect:/profile/" + principal.getName() + "/editprofile";
            }


            userImpl.changePassword(principal.getName(), changePasswordDtoForm.getOldPass(), changePasswordDtoForm.getNewPassword());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contraseña de usuario {0} cambiada", principal.getName()));
            }
        } catch (AuthenticationException e) {
            serviceRedirectExceptions.serviceAuthenticationException(e, redirectAttributes);
            return "redirect:/profile/" + principal.getName() + "/editprofile";
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (UsernameNotFound ex) {
            return serviceExceptions.serviceUsernameNotFoundException(ex, model);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "changepassword.success", new Object[]{principal.getName()}, locale));
        return "redirect:/profile/" + principal.getName() + "/editprofile";
    }

    @PostMapping("/editprofile/modify")
    public String doPostModifyProfile(Principal principal, @Valid @ModelAttribute("EditProfileDtoForm") ProfileDtoForm editProfileDtoForm,
                                      BindingResult result, Locale locale,  RedirectAttributes redirectAttributes,
                                      Model model) {
        model.addAttribute("UpdateImageProfileDtoForm", new UpdateImageProfileDtoForm());
        model.addAttribute("ChangePasswordDtoForm", new ChangePasswordDtoForm());

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result,"modifyprofile.invalid.parameters",principal.getName(),locale,redirectAttributes);
            return "redirect:/profile/" + editProfileDtoForm.getUsername() + "/editprofile";
        }

        try {
            userImpl.modifyProfile(principal.getName(), editProfileDtoForm.getUsername(), editProfileDtoForm.getEmail());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Perfil de usuario {0} modificado", principal.getName()));
            }
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex,redirectAttributes);
            return "redirect:/profile/" + editProfileDtoForm.getUsername() + "/editprofile";
        } catch (UsernameNotFound ex) {
            return serviceExceptions.serviceUsernameNotFoundException(ex, model);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "modifyprofile.success", new Object[]{principal.getName()}, locale));
         return "redirect:/profile/" + editProfileDtoForm.getUsername() + "/editprofile";
    }

    @PostMapping("/editprofile/updatephoto")
    public String doPostUpdatePhoto(Principal principal, @Valid @ModelAttribute("UpdateImageProfileDtoForm") UpdateImageProfileDtoForm updateImageProfileDtoForm,
                                    Locale locale, BindingResult result,
                                    Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("ChangePasswordDtoForm", new ChangePasswordDtoForm());
        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result,"updatephoto.invalid.parameters",principal.getName(),locale,redirectAttributes);
            return "redirect:/profile/" + principal.getName() + "/editprofile";
        }
        try {
            userImpl.updateProfileImage(principal.getName(), updateImageProfileDtoForm.getImageFile() != null ? updateImageProfileDtoForm.getImageFile().getOriginalFilename() : null,
                    updateImageProfileDtoForm.getImageFile() != null ? updateImageProfileDtoForm.getImageFile().getBytes() : null);
            Users user = userImpl.findByUsername(principal.getName());
            byte[] image = userImpl.getImage(user.getUserId());
            ProfileDtoForm editProfileDtoForm = UserConversor.toProfile(user, image, user.getImagen_perfil());
            model.addAttribute("EditProfileDtoForm", editProfileDtoForm);
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Fotografía de usuario {0} cambiada", principal.getName()));
            }
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (UsernameNotFound ex) {
            return serviceExceptions.serviceUsernameNotFoundException(ex, model);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "photoprofile.success", new Object[]{principal.getName()}, locale));
        return "redirect:/profile/" + principal.getName() + "/editprofile";
    }


}




