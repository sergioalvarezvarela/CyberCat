package es.cybercatapp.service.Controllers;

import com.google.protobuf.Empty;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.ContentImpl;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.ContentDtoForm;
import es.cybercatapp.service.dto.ListModuleDtoForm;
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

import javax.naming.Name;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ModuleOperations {

    private static final Logger logger = LoggerFactory.getLogger(UserOperations.class);

    @Autowired
    private MessageSource messageSource;
    @Autowired
    ServiceExceptions serviceExceptions;

    @Autowired
    ServiceRedirectExceptions serviceRedirectExceptions;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private CourseImpl courseImpl;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/managecourses/{id}/editcourses"})
    public String doGetCourseContent(@PathVariable("id") String id, Model model, Locale locale) {
        try {
            model.addAttribute("ModuleDtoForm", new ModuleDtoForm());
            model.addAttribute("ContentDtoForm", new ContentDtoForm());
            Courses course = courseImpl.findCoursesById(Long.parseLong(id));
            List<Module> modules = course.getModules();
            List<ModuleDtoForm> moduleDto = new ArrayList<>();



            for (Module module : modules) {
                String moduleId = module.getId().getModuleName();
                moduleId = moduleId.replaceAll("\\s+", "");
                List<ContentDtoForm> contentDto = new ArrayList<>();
                for (Content contents : module.getContents()) {
                    String contentId = contents.getContentId().getContentName();
                    contentId = contentId.replaceAll("\\s+", "");
                    contentDto.add(new ContentDtoForm(contentId,contents.getContentId().getContentName(),module.getId().getModuleName(), 0));
                }
                moduleDto.add(new ModuleDtoForm(moduleId, module.getId().getModuleName(), module.getModulePosition(),contentDto));

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
                                   BindingResult result, Locale locale, RedirectAttributes redirectAttributes, Model model, HttpSession session) {


        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "addmodule.invalid.parameters", moduleDtoForm.getModuleName(), locale, redirectAttributes);
            return "redirect:/managecourses/" + id + "/editcourses";
        }

        Module module;
        try {
            module = moduleImpl.create(moduleDtoForm.getModuleName(), Long.valueOf(id));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Modulo {0} con id {1} creado", module.getId().getModuleName(), module.getId()));
            }
            session.setAttribute(Constants.USER_SESSION, module);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addmodule.success", new Object[]{module.getId()}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        return "redirect:/managecourses/" + id + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseid}/editcourses/removemodule/{moduleName}"})
    public String doPostRemoveModule(@PathVariable("courseid") String courseid, @PathVariable("moduleName") String moduleName, Model model, Locale locale, RedirectAttributes redirectAttributes) {

        moduleName = moduleName.replace("%20", " ");
        try {
            moduleImpl.remove(Long.valueOf(courseid), moduleName);
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removemodule.success", new Object[]{moduleName}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseid}/editcourses/updatemodule/{moduleName}"})
    public String doPostUpdateModule(@PathVariable("courseid") String courseid, @PathVariable("moduleName") String moduleName, @Valid @ModelAttribute("ModuleDtoForm") ModuleDtoForm moduleDtoForm,
                                     BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "updatemodule.invalid.parameters", moduleDtoForm.getModuleName(), locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        }

        moduleName = moduleName.replace("%20", " ");

        try {
            moduleImpl.update(Long.valueOf(courseid), moduleName, moduleDtoForm.getModuleName());
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editmodule.success", new Object[]{moduleName}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{id}/editcourses/updatepositions"})
    public String doPostUpdateModulePositions(@PathVariable("id") String id, @ModelAttribute("ListModuleDtoForm") ListModuleDtoForm listModuleDtoForm,
                                              Locale locale, Model model, RedirectAttributes redirectAttributes) {
        try {
            Courses courses = courseImpl.findCoursesById(Long.parseLong(id));

            List<String> moduleNames = listModuleDtoForm.getModuleNames();
            List<Module> modules = courses.getModules();

            modules.sort(Comparator.comparingInt(module -> moduleNames.indexOf(module.getId().getModuleName())));
            for (int i = 0; i < modules.size(); i++) {
                modules.get(i).setModulePosition(i + 1);
            }
            courses.setModules(modules);
            courseImpl.updatePositions(courses);
        } catch (InstanceNotFoundException ex) {
            serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editpositions.success", new Object[]{id}, locale));
        return "redirect:/managecourses/" + id + "/editcourses";
    }

}
