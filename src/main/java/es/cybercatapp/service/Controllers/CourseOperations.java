package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.conversor.CoursesConversor;
import es.cybercatapp.service.dto.AddModifyCourseDtoForm;
import es.cybercatapp.service.dto.CourseDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class CourseOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    private CourseImpl courseImpl;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/managecourses"})
    public String doGetCourseManagement(Model model, Principal principal, Locale locale) {

        try {


            List<Courses> courses = courseImpl.findCoursesByOwner(principal.getName());
            List<CourseDtoForm> courseDtos = new ArrayList<>();

            for (Courses course : courses) {
                byte[] image = courseImpl.getImage(course.getCourseId());
                courseDtos.add(CoursesConversor.toCourseDtoForm(course, image, course.getCourse_photo()));
            }
            model.addAttribute("username",principal.getName());
            model.addAttribute("CourseDtoForm", courseDtos);
            model.addAttribute("AddModifyCourseDtoForm", new AddModifyCourseDtoForm());
            model.addAttribute("category", Category.values());
        } catch (InstanceNotFoundException ex) {
            serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

        return "managecourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/addcourse"})
    public String doPostAddCourse(@Valid @ModelAttribute("AddModifyCourseDtoForm") AddModifyCourseDtoForm addModifyCourseDtoForm,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes, Principal principal, Locale locale,
                                  HttpSession session, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(Constants.ERROR_MESSAGE, messageSource.getMessage(
                    "addcourse.invalid.parameters", null, locale));
            return "managecourses";
        }
        Courses course;
        try {
            course = courseImpl.create(addModifyCourseDtoForm.getCourseName(), addModifyCourseDtoForm.getPrice(), addModifyCourseDtoForm.getCategory(),
                    addModifyCourseDtoForm.getImage() != null ? addModifyCourseDtoForm.getImage().getOriginalFilename() : null,
                    addModifyCourseDtoForm.getImage() != null ? addModifyCourseDtoForm.getImage().getBytes() : null, addModifyCourseDtoForm.getDescription(), principal.getName());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Course {0} with id {1} created", course.getCourse_name(), course.getCourseId()));
            }
            session.setAttribute(Constants.USER_SESSION, course);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addcourse.success", new Object[]{course.getCourse_name()}, locale));
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + "/managecourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/managecourses/remove/{id}")
    public String doPostRemoveCourses(@PathVariable String id, Locale locale, RedirectAttributes redirectAttributes, Model model) {
        try {
            courseImpl.remove(Long.parseLong(id));
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "removecourses.success", new Object[]{id}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        return Constants.SEND_REDIRECT + "/managecourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/managecourses/update/{id}")
    public String doPostUpdateCourseInfo(@PathVariable String id, @Valid @ModelAttribute("AddModifyCourseDtoForm") AddModifyCourseDtoForm addModifyCourseDtoForm,
                                         BindingResult result,
                                         RedirectAttributes redirectAttributes, Locale locale, Model model, Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute(Constants.ERROR_MESSAGE, messageSource.getMessage(
                    "updatecourse.invalid.parameters", null, locale));
            return "managecourses";
        }
        try {
            courseImpl.update(Long.parseLong(id), addModifyCourseDtoForm.getCourseName(), addModifyCourseDtoForm.getPrice(), addModifyCourseDtoForm.getCategory(),
                    addModifyCourseDtoForm.getImage() != null ? addModifyCourseDtoForm.getImage().getOriginalFilename() : null,
                    addModifyCourseDtoForm.getImage() != null ? addModifyCourseDtoForm.getImage().getBytes() : null, addModifyCourseDtoForm.getDescription(), principal.getName());
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "updatecourse.success", new Object[]{id}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (IOException ex) {
            return serviceExceptions.serviceUnexpectedException(ex, model);
        }
        return Constants.SEND_REDIRECT + "/managecourses";
    }

}


