package es.cybercatapp.service.Controllers;

import es.cybercatapp.model.entities.*;
import es.cybercatapp.service.dto.StringCompleteDtoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.impl.ContentImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.service.Exceptions.ServiceExceptions;
import es.cybercatapp.service.Exceptions.ServiceRedirectExceptions;
import es.cybercatapp.service.dto.ContentDtoForm;
import es.cybercatapp.service.dto.ListContentDtoForm;
import es.cybercatapp.service.dto.TeoricDtoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.*;

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
    public String doPostAddContent(@PathVariable("courseId") String courseId, @PathVariable("moduleId") String moduleId, @Valid @ModelAttribute("ContentDtoForm") ContentDtoForm addContentDtoForm,
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
            if (addContentDtoForm.getContentType().equals("select")) {
                String enunciado = "Enunciado de prueba";
                content = contentImpl.createTestQuestionContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), enunciado);
            }
            if (addContentDtoForm.getContentType().equals("puzzle")) {
                String enunciado = "Enunciado de prueba";
                String frase = "Enunciado de __ para test";
                String frasecorrecta = "Enunciado de ejemplo para test";
                String words = "ejemplo\nPalabra1";
                content = contentImpl.createStringCompleteContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), enunciado, frase, frasecorrecta, words);
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
    public String doPostRemoveContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @ModelAttribute("ListContentDtoForm") ListContentDtoForm listContentDtoForm, BindingResult result,
                                      RedirectAttributes redirectAttributes, Locale locale,
                                      Model model) {

        try {
            contentImpl.remove(Long.valueOf(contentId));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "removecontent.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/updatecontent/{contentId}"})
    public String doPostUpdateContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @Valid @ModelAttribute("ContentDtoForm") ContentDtoForm contentDtoForm, BindingResult result,
                                      RedirectAttributes redirectAttributes, Locale locale,
                                      Model model) {
        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "updatecontent.invalid.parameters", contentId, locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        }
        try {
            contentImpl.contentUpdate(Long.valueOf(moduleId), Long.valueOf(contentId), contentDtoForm.getContentName());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses";
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "updatecontent.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/editcontent/{contentId}"})
    public String doGetEditContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @ModelAttribute("ContentDtoForm") ContentDtoForm contentDtoForm, BindingResult result,
                                   Locale locale,
                                   Model model) {
        try {
            if (contentImpl.findByContentId(Long.valueOf(contentId)) instanceof StringContent) {
                StringContent content = (StringContent) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("TeoricDtoFormShow", new TeoricDtoForm(content.getContentId(), Long.parseLong(moduleId), Long.parseLong(courseid), content.getHtml(), content.getContent_category().getDescripcion()));
                model.addAttribute("TeoricDtoForm", new TeoricDtoForm());
                return "teoricContent";
            } else if (contentImpl.findByContentId(Long.valueOf(contentId)) instanceof StringComplete) {
                StringComplete content = (StringComplete) contentImpl.findByContentId(Long.parseLong(contentId));
                content = contentImpl.initializeStringOptions(content);
                StringBuilder words = new StringBuilder();
                for (StringCompleteOptions word : content.getStringCompleteOptions()) {
                    words.append(word.getStringCompleteOption());
                    words.append("\n");
                }
                model.addAttribute("StringCompleteDtoFormShow", new StringCompleteDtoForm(content.getContentId(), Long.parseLong(moduleId), Long.parseLong(courseid), content.getEnunciado(), content.getSentence(), content.getCorrectSentence(), words.toString()));
                return "createPuzzle";
            } else{
                TestQuestions content = (TestQuestions) contentImpl.findByContentId(Long.parseLong(contentId));
                return null;
            }

        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/editcontent/{contentId}/saveteoric"})
    public String doPostEditTeoricContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @Valid @ModelAttribute("TeoricDtoForm") TeoricDtoForm teoricDtoForm, BindingResult result,
                                    RedirectAttributes redirectAttributes, Locale locale,
                                    Model model) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "editcontent.invalid.parameters", contentId, locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
        }

        try {
            contentImpl.stringContentUpdate(teoricDtoForm.getHtml(), Long.parseLong(contentId));
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editcontent.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/editcontent/{contentId}/savepuzzle"})
    public String doPostEditPuzzleContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @Valid @ModelAttribute("StringCompleteDtoForm") StringCompleteDtoForm stringCompleteDtoForm, BindingResult result,
                                          RedirectAttributes redirectAttributes, Locale locale,
                                          Model model) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "editcontent.invalid.parameters", contentId, locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
        }

        try {
            contentImpl.puzzleContentUpdate(stringCompleteDtoForm.getContentId(), stringCompleteDtoForm.getEnunciado(), stringCompleteDtoForm.getFrase(), stringCompleteDtoForm.getFraseCorrecta(), stringCompleteDtoForm.getWords());
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        } catch (DuplicatedResourceException ex) {
            serviceRedirectExceptions.serviceDuplicatedResourceException(ex, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
        }
        redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                "editcontent.success", new Object[]{moduleId}, locale));
        return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
    }


}
