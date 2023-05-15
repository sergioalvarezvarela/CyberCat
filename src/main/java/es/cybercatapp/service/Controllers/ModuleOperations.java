package es.cybercatapp.service.Controllers;

import es.cybercatapp.service.dto.CatalogDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Modules;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.ContentDtoForm;
import es.cybercatapp.service.dto.ListModuleDtoForm;
import es.cybercatapp.service.dto.ModuleDtoForm;
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
import java.util.*;

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
            model.addAttribute("AddContentDtoForm", new ContentDtoForm());
            Courses course = courseImpl.findCoursesById(Long.parseLong(id));
            List<Modules> modules = course.getModules();
            List<ModuleDtoForm> moduleDto = new ArrayList<>();


            for (Modules module : modules) {
                Long moduleId = module.getModuleId();
                List<ContentDtoForm> contentDto = new ArrayList<>();
                for (Content contents : module.getContents()) {
                    contentDto.add(new ContentDtoForm(contents.getContentId(), contents.getContentName(), module.getModuleName(), contents.getContentPosition(), contents.getContent_category().getDescripcion(),null));
                }
                moduleDto.add(new ModuleDtoForm(moduleId, module.getModuleName(), module.getModulePosition(), contentDto, null));

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

        Modules modules;
        try {
            modules = moduleImpl.create(moduleDtoForm.getModuleName(), Long.valueOf(id));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Modulo {0} con id {1} creado", modules.getModuleName(), modules.getModuleId()));
            }
            session.setAttribute(Constants.USER_SESSION, modules);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addmodule.success", new Object[]{modules.getModuleId()}, locale));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + id + "/editcourses";
        }
        return "redirect:/managecourses/" + id + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseid}/editcourses/removemodule/{moduleId}"})
    public String doPostRemoveModule(@PathVariable("courseid") String courseid, @PathVariable("moduleId") String moduleId, Model model, Locale locale, RedirectAttributes redirectAttributes) {

        try {
            moduleImpl.remove(Long.valueOf(moduleId));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removemodule.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseid}/editcourses/updatemodule/{moduleId}"})
    public String doPostUpdateModule(@PathVariable("courseid") String courseid, @PathVariable("moduleId") String moduleId, @Valid @ModelAttribute("ModuleDtoForm") ModuleDtoForm moduleDtoForm,
                                     BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "updatemodule.invalid.parameters", moduleDtoForm.getModuleName(), locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        }


        try {
            moduleImpl.update(Long.valueOf(courseid), Long.valueOf(moduleId), moduleDtoForm.getModuleName());
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editmodule.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{id}/editcourses/updatepositions"})
    public String doPostUpdateModulePositions(@PathVariable("id") String id, @ModelAttribute("ListModuleForm") ListModuleDtoForm listModuleDtoForm,
                                              Locale locale, Model model, RedirectAttributes redirectAttributes) {
        try {
            Courses courses = courseImpl.findCoursesById(Long.parseLong(id));

            List<Long> moduleIds = listModuleDtoForm.getModuleIds();
            List<Modules> modules = courses.getModules();

            modules.sort(Comparator.comparingInt(module -> moduleIds.indexOf(module.getModuleId())));
            for (int i = 0; i < modules.size(); i++) {
                modules.get(i).setModulePosition(i + 1);
            }
            courses.setModules(modules);
            courseImpl.updatePositions( courses);
        } catch (InstanceNotFoundException ex) {
            serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editmodulepositions.success", new Object[]{id}, locale));
        return "redirect:/managecourses/" + id + "/editcourses";
    }

}
