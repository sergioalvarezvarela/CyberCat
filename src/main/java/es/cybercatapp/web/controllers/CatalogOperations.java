package es.cybercatapp.web.controllers;


import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.services.CourseImpl;
import es.cybercatapp.model.services.InscriptionsImpl;
import es.cybercatapp.web.exceptions.ServiceExceptions;
import es.cybercatapp.web.exceptions.ServiceRedirectExceptions;
import es.cybercatapp.web.conversor.CoursesConversor;
import es.cybercatapp.web.dto.CatalogDtoForm;
import es.cybercatapp.web.dto.CourseDtoForm;
import es.cybercatapp.web.dto.LoginDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class CatalogOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private InscriptionsImpl inscrImpl;

    @GetMapping(value = {"/catalog"})
    public String doGetCatalog(@RequestParam("start") String start, @RequestParam("filter") String filter, @RequestParam("category") String category, @RequestParam("word") String word, @ModelAttribute("CatalogDtoForm") CatalogDtoForm catalogDtoForm, Model model, Principal principal, Locale locale) {

        try {

            List<Courses> courses = courseImpl.findAllFiltered(Integer.parseInt(start), Integer.parseInt(filter), category, word);
            List<CourseDtoForm> courseDtos = new ArrayList<>();

            for (Courses course : courses) {
                byte[] image = courseImpl.getImage(course.getCourseId());
                courseDtos.add(CoursesConversor.toCourseDtoForm(course, image, course.getCourse_photo()));
            }
            model.addAttribute("CourseDtoForm", courseDtos);
            model.addAttribute("CatalogDtoForm", new CatalogDtoForm(Integer.parseInt(start), Integer.parseInt(filter), word, category));
            model.addAttribute("principal", principal);
            model.addAttribute("LoginDtoForm", new LoginDtoForm());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }


        return "catalog";
    }

    @PostMapping(value = {"/sigon/{courseId}"})
    public String doPostSignOn(@PathVariable("courseId") String courseid, Principal principal,
                               RedirectAttributes redirectAttributes, Locale locale,
                               Model model) {

        try {
            inscrImpl.signOn(principal.getName(), Long.valueOf(courseid));

        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/catalog?start=0&count=5&category=Todos&word=&filter=1";
        } catch (UsernameNotFound ex) {
            return serviceExceptions.serviceUsernameNotFoundException(ex, model);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "inscription.success", new Object[]{principal.getName()}, locale));

        return "redirect:/catalog?start=0&count=5&category=Todos&word=&filter=1";
    }


}
