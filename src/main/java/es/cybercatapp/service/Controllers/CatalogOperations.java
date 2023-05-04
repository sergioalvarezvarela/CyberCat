package es.cybercatapp.service.Controllers;


import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.ContentImpl;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.conversor.CoursesConversor;
import es.cybercatapp.service.dto.CourseDtoForm;
import es.cybercatapp.service.dto.PaginationDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = {"/catalog"})
    public String doGetCatalog(@RequestParam("start") String start, @RequestParam("count") String count,  Model model, Principal principal, Locale locale) {

        try {

            List<Courses> courses = courseImpl.findAll(Integer.parseInt(start), Integer.parseInt(count));
            List<CourseDtoForm> courseDtos = new ArrayList<>();

            for (Courses course : courses) {
                byte[] image = courseImpl.getImage(course.getCourseId());
                courseDtos.add(CoursesConversor.toCourseDtoForm(course, image, course.getCourse_photo()));
            }
            model.addAttribute("CourseDtoForm", courseDtos);
            model.addAttribute("PaginationDtoForm", new PaginationDtoForm(Integer.parseInt(start), Integer.parseInt(count)));
            model.addAttribute("principal", principal);
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

        return "catalog";
    }

}
