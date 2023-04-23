package es.cybercatapp.service.Controllers;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Modules;
import es.cybercatapp.model.entities.StringContent;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.ContentImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.AddContentDtoForm;
import es.cybercatapp.service.dto.ContentDtoForm;
import es.cybercatapp.service.dto.ListContentDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
public class ContentOperations {

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
    private ContentImpl contentImpl;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/addcontent"})
    public String doPostAddContent(@PathVariable("courseId") String courseId, @PathVariable("moduleId") String moduleId, @ModelAttribute("AddContentDtoForm") AddContentDtoForm addContentDtoForm,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes, Locale locale,
                                   HttpSession session, Model model) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "addcontent.invalid.parameters", moduleId, locale, redirectAttributes);
            return "redirect:/managecourses/" + courseId + "/editcourses";
        }

        Content content = null;
        try {
            if (addContentDtoForm.getContentType().equals("teoric")) {
                String html = "Lorem Ipsum";
                content = contentImpl.createTeoricContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), html);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido {0} con id {1} creado", addContentDtoForm.getContentName(), content.getContentId()));
            }
            session.setAttribute(Constants.USER_SESSION, content);
            redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                    "addcontent.success", new Object[]{content.getContentId()}, locale));

        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseId + "/editcourses";
        }
        return "redirect:/managecourses/" + courseId + "/editcourses";

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/updatepositions"})
    public String doPostUpdateModulePositions(@PathVariable("courseId") String courseId, @PathVariable("moduleId") String moduleId, @ModelAttribute("ListContentDtoForm") ListContentDtoForm listContentDtoForm,
                                              Locale locale, Model model, RedirectAttributes redirectAttributes) {
        try {
            Modules module = moduleImpl.findModulesById(Long.parseLong(moduleId));

            List<Long> contentIds = listContentDtoForm.getContentIds();
            List<Content> contents = module.getContents();

            contents.sort(Comparator.comparingInt(content -> contentIds.indexOf(content.getContentId())));
            for (int i = 0; i < contents.size(); i++) {
                contents.get(i).setContentPosition(i + 1);
            }
            module.setContents(contents);
            moduleImpl.updatePositions(module);
        } catch (InstanceNotFoundException ex) {
            serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editcontentpositions.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseId + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/removecontent/{contentId}"})
    public String doPostRemoveContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId,@PathVariable("contentId") String contentId , Model model, Locale locale, RedirectAttributes redirectAttributes) {

        try {
            contentImpl.remove(Long.valueOf(contentId));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removecontent.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }
}
