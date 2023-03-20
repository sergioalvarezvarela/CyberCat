package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.dto.ModuleDtoForm;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class ModuleOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    private ModuleImpl moduleImpl;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/managecourses/{id}/editcourses"})
    public String doGetCourseContent(@PathVariable("id") String id , Model model, Locale locale) {
        try {
            model.addAttribute("ModuleDtoForm", new ModuleDtoForm());

            List<Module> modules = moduleImpl.findModulesByCourse(Long.valueOf(id));
            List<ModuleDtoForm> moduleDto = new ArrayList<>();

            for (Module module : modules) {
                moduleDto.add(new ModuleDtoForm(module.getModuleId(),module.getModule_name()));
            }
            model.addAttribute("ModuleDtoList", moduleDto);

            model.addAttribute("courseId", id);
        } catch (InstanceNotFoundException ex) {
            serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

        return "editmodule";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{id}/editcourses/addmodule"})
    public String doPostAddModule(@PathVariable("id") String id, @Valid @ModelAttribute("ModuleDtoForm") ModuleDtoForm moduleDtoForm,
                                 Locale locale, BindingResult result, RedirectAttributes redirectAttributes, Model model, HttpSession session) {

        if (result.hasErrors()) {
            serviceExceptions.serviceInvalidFormError(result,
                    "addmodule.invalid.parameters", model, locale);
            return "redirect:/managecourses/" + id + "/editcourses";
        }

        Module module;
        try {
            module = moduleImpl.create(moduleDtoForm.getModuleName(), Long.valueOf(id));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Modulo {0} con id {1} creado", module.getModule_name(), module.getModuleId()));
            }
            session.setAttribute(Constants.USER_SESSION, module);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addmodule.success", new Object[]{module.getModule_name()}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex,model,locale);
        }
        return "redirect:/managecourses/" + id + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{id}/editcourses/removemodule"})
    public String doPostRemoveModule(@PathVariable("id") String id, @ModelAttribute("ModuleDtoForm") ModuleDtoForm moduleDtoForm, Model model, Locale locale) {

        try{
            moduleImpl.remove(moduleDtoForm.getId());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex,model,locale);
        }
        return "editcourses";
    }
}
