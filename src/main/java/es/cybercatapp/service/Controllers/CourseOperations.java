package es.cybercatapp.service.Controllers;

import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.dto.RegisterDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;


    @Autowired
    private UserImpl userImpl;

    @GetMapping(value = {"/managecourses"})
    public String doGetCourseManagement(Model model) {

        model.addAttribute("category", Category.values());
        return "managecourses";
    }
}
