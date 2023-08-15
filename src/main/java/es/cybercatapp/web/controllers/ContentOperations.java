package es.cybercatapp.web.controllers;

import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.services.ContentUserImpl;
import es.cybercatapp.web.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.services.ContentImpl;
import es.cybercatapp.model.services.ModuleImpl;
import es.cybercatapp.web.exceptions.ServiceExceptions;
import es.cybercatapp.web.exceptions.ServiceRedirectExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
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

    @Autowired
    private ContentUserImpl contentUserImpl;

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
            String enunciado = "Enunciado de prueba";
            if (addContentDtoForm.getContentType().equals("teoric")) {
                String html = "Lorem Ipsum";
                content = contentImpl.createTeoricContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), html);
            } else if (addContentDtoForm.getContentType().equals("puzzle")) {
                String frase = "Enunciado de __ para test";
                String frasecorrecta = "Enunciado de ejemplo para test";
                String words = "ejemplo\nPalabra1";
                content = contentImpl.createStringCompleteContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), enunciado, frase, frasecorrecta, words);
            } else {
                content = contentImpl.createTestQuestionContent(Long.valueOf(moduleId), addContentDtoForm.getContentName(), enunciado);
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
    public String doPostUpdateContentPositions(@PathVariable("courseId") String courseId, @PathVariable("moduleId") String moduleId, @ModelAttribute("ListContentDtoForm") ListContentDtoForm listContentDtoForm,
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
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Posiciones de modulo con id {0} actualizadas", moduleId));
            }
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
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Modulo con id {0} eliminado", moduleId));
            }
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
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} actualizado", contentId));
            }
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
                model.addAttribute("TeoricDtoForm", new TeoricDtoForm(content.getContentId(), Long.parseLong(moduleId), Long.parseLong(courseid), content.getHtml(), content.getContent_category().getDescripcion()));
                return "teoricContent";
            } else if (contentImpl.findByContentId(Long.valueOf(contentId)) instanceof StringComplete) {
                StringComplete content = (StringComplete) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("StringCompleteDtoForm", new StringCompleteDtoForm(content.getContentId(), Long.parseLong(moduleId), Long.parseLong(courseid), content.getEnunciado(), content.getSentence(), content.getCorrectSentence(), content.getContent()));
                return "createPuzzle";
            } else {
                TestQuestions content = (TestQuestions) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("TestOptionsDtoForm", new TestOptionsDtoForm(content.getContentId(), Long.parseLong(moduleId), Long.parseLong(courseid), content.getQuestion(), content.getOption1(), content.getOption2(), content.getOption3(), content.getOption4(), content.getCorrect()));
                return "testoption";
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
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} actualizado", contentId));
            }
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
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} actualizado", contentId));
            }
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
    @PostMapping(value = {"/managecourses/{courseId}/editcourses/{moduleId}/editcontent/{contentId}/savetest"})
    public String doPostEditTestContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId, @Valid @ModelAttribute("TestOptionsDtoForm") TestOptionsDtoForm testOptionsDtoForm, BindingResult result,
                                        RedirectAttributes redirectAttributes, Locale locale,
                                        Model model) {

        if (result.hasErrors()) {
            serviceRedirectExceptions.serviceInvalidFormError(result, "editcontent.invalid.parameters", contentId, locale, redirectAttributes);
            return "redirect:/managecourses/" + courseid + "/editcourses/" + moduleId + "/editcontent/" + contentId;
        }

        try {
            contentImpl.testContentUpdate(Long.valueOf(contentId), testOptionsDtoForm.getEnunciado(), testOptionsDtoForm.getOpcion1(), testOptionsDtoForm.getOpcion2(), testOptionsDtoForm.getOpcion3(), testOptionsDtoForm.getOpcion4(), testOptionsDtoForm.getSelectedOption());
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} actualizado", contentId));
            }
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


    @GetMapping(value = {"/course/{courseId}/module/{moduleId}/learn/{contentId}"})
    public String doGetSeeContent(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleid, @PathVariable("contentId") String contentId, @RequestParam("contenttype") String contentType, Principal principal, Locale locale,
                                   Model model, HttpServletRequest request) {
        model.addAttribute("courseId", Long.valueOf(courseid));
        model.addAttribute("moduleId", Long.valueOf(moduleid));


        try {
            Modules modules = moduleImpl.findModulesById(Long.parseLong(moduleid));
            if (contentType.equals("teoric")) {
                StringContent content = (StringContent) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("TeoricDtoForm", new TeoricDtoForm(content.getContentId(), content.getModule().getModuleId(), Long.parseLong(courseid), content.getHtml(), content.getContent_category().getDescripcion()));
                Content before = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() - 1);
                if (before != null) {
                    model.addAttribute("before", new ContentDtoForm(before.getContentId(), before.getContentName(), before.getModule().getModuleName(), before.getContentPosition(), before.getContent_category().getDescripcion(), null));
                }
                Content next = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() + 1);
                if (next != null) {
                    model.addAttribute("next", new ContentDtoForm(next.getContentId(), next.getContentName(), next.getModule().getModuleName(), next.getContentPosition(), next.getContent_category().getDescripcion(), null));
                }
                model.addAttribute("progress", Math.round((float) (content.getContentPosition() - 1) / modules.getContents().size() * 100));
                contentUserImpl.updateUserContent(principal.getName(), Long.parseLong(contentId),true);
                return "seeTeoricContent";
            } else if (contentType.equals("select")) {
                TestQuestions content = (TestQuestions) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("TestOptionsDtoForm", new TestOptionsDtoForm(content.getContentId(), content.getModule().getModuleId(), Long.parseLong(courseid), content.getQuestion(), content.getOption1(), content.getOption2(), content.getOption3(), content.getOption4(), content.getCorrect()));

                Content before = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() - 1);
                if (model.getAttribute("TestCheckDtoForm")!=null){
                    model.getAttribute("TestCheckDtoForm");
                }else {
                    model.addAttribute("TestCheckDtoForm",new TestCheckDtoForm());
                }
                if (before != null) {
                    model.addAttribute("before", new ContentDtoForm(before.getContentId(), before.getContentName(), before.getModule().getModuleName(), before.getContentPosition(), before.getContent_category().getDescripcion(), null));
                }
                Content next = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() + 1);
                if (next != null) {
                    model.addAttribute("next", new ContentDtoForm(next.getContentId(), next.getContentName(), next.getModule().getModuleName(), next.getContentPosition(), next.getContent_category().getDescripcion(), null));
                }
                model.addAttribute("progress", Math.round((float) (content.getContentPosition() - 1) / modules.getContents().size() * 100));
                return "seeTestContent";
            } else{
                StringComplete content = (StringComplete) contentImpl.findByContentId(Long.parseLong(contentId));
                model.addAttribute("StringCompleteDtoForm", new StringCompleteDtoForm(content.getContentId(), content.getModule().getModuleId(), Long.parseLong(courseid), content.getEnunciado(), content.getSentence(),content.getCorrectSentence(),content.getContent()));
                Content before = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() - 1);
                if (model.getAttribute("PuzzleCheckDtoForm")!=null){
                    model.getAttribute("PuzzleCheckDtoForm");
                }else {
                    model.addAttribute("PuzzleCheckDtoForm",new PuzzleCheckDtoForm());
                }
                if (before != null) {
                    model.addAttribute("before", new ContentDtoForm(before.getContentId(), before.getContentName(), before.getModule().getModuleName(), before.getContentPosition(), before.getContent_category().getDescripcion(), null));
                }
                Content next = contentImpl.findContentByModuleIdAndPosition(Long.parseLong(moduleid), content.getContentPosition() + 1);
                if (next != null) {
                    model.addAttribute("next", new ContentDtoForm(next.getContentId(), next.getContentName(), next.getModule().getModuleName(), next.getContentPosition(), next.getContent_category().getDescripcion(), null));
                }
                model.addAttribute("progress", Math.round((float) (content.getContentPosition() - 1) / modules.getContents().size() * 100));
                return "seePuzzleContent";

            }
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }
    }

    @PostMapping(value = {"/course/{courseId}/module/{moduleId}/learn/{contentId}/checktest"})
    public String doPostCheckTestCorrect(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId,@ModelAttribute("TestCheckDtoForm") TestCheckDtoForm testCheckDtoForm, BindingResult result,
                                     RedirectAttributes redirectAttributes, Locale locale,
                                     Model model, Principal principal) {


        try {
            if (testCheckDtoForm.getSelectedOption() == null) {
                serviceRedirectExceptions.serviceInvalidFormError(result, "checktest.invalid.parameters", contentId, locale, redirectAttributes);
                return "redirect:/course/" + courseid + "/module/" + moduleId + "/learn/" + contentId + "?contenttype=select" ;
            }
            boolean correct = contentImpl.isTestCorrect(Long.parseLong(contentId), testCheckDtoForm.getSelectedOption());
            contentUserImpl.updateUserContent(principal.getName(), Long.parseLong(contentId), correct);
            redirectAttributes.addFlashAttribute("TestCheckDtoForm", new TestCheckDtoForm(testCheckDtoForm.getSelectedOption(),correct));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} corregido", contentId));
            }
            return "redirect:/course/" + courseid + "/module/" + moduleId + "/learn/" + contentId + "?contenttype=select";
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

    }


    @PostMapping(value = {"/course/{courseId}/module/{moduleId}/learn/{contentId}/checkpuzzle"})
    public String doPostCheckPuzzleCorrect(@PathVariable("courseId") String courseid, @PathVariable("moduleId") String moduleId, @PathVariable("contentId") String contentId,@ModelAttribute("PuzzleCheckDtoForm") PuzzleCheckDtoForm puzzleCheckDtoForm, BindingResult result,
                                     RedirectAttributes redirectAttributes, Locale locale,
                                     Model model, Principal principal) {


        try {
            if (puzzleCheckDtoForm.getWords() == null) {
                serviceRedirectExceptions.serviceInvalidFormError(result, "checktest.invalid.parameters", contentId, locale, redirectAttributes);
                return "redirect:/course/" + courseid + "/module/" + moduleId + "/learn/" + contentId + "?contenttype=puzzle" ;
            }
            PuzzleCheckReturn puzzleCheckReturn = contentImpl.isPuzzleCorrect(Long.parseLong(contentId), puzzleCheckDtoForm.getWords());
            contentUserImpl.updateUserContent(principal.getName(), Long.parseLong(contentId), puzzleCheckReturn.isCorrect());
            redirectAttributes.addFlashAttribute("PuzzleCheckDtoForm", new PuzzleCheckDtoForm(puzzleCheckReturn.getFormatedSentence(),puzzleCheckReturn.isCorrect()));
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("Contenido con id {0} corregido", contentId));
            }
            if (puzzleCheckReturn.isCorrect()){
                redirectAttributes.addFlashAttribute(Constants.SUCCESS_MESSAGE, messageSource.getMessage(
                        "puzzlecontent.success", new Object[]{moduleId}, locale));
            } else{
                redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, messageSource.getMessage(
                        "puzzlecontent.error", new Object[]{moduleId}, locale));
            }
            return "redirect:/course/" + courseid + "/module/" + moduleId + "/learn/" + contentId + "?contenttype=puzzle";
        } catch (InstanceNotFoundException ex) {
            return serviceExceptions.serviceInstanceNotFoundException(ex, model, locale);
        }

    }
}
