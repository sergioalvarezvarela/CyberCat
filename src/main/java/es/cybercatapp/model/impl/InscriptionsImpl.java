package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.repositories.*;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service(value = "inscriptionsImpl")
public class InscriptionsImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InscriptionsRepository inscriptionsRepository;

    @Autowired
    private ContentUserRepository contentUserRepository;

    @Autowired
    private ModuleUserRepository moduleUserRepository;

    @Autowired
    private ModuleUserImpl moduleUserImpl;

    @Autowired
    private ContentUserImpl contentUserImpl;

    @Transactional
    public void signOn(String username, Long courseId) throws InstanceNotFoundException, DuplicatedResourceException, UsernameNotFound {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw exceptionGenerationUtils.toUsernameNotFoundException(Constants.USERNAME_FIELD, username, "user.not.found");
        } else {
            Inscriptions inscriptions;
            inscriptions = inscriptionsRepository.findInscription(courseId, username);
            if (inscriptions != null) {
                throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.USERNAME_FIELD, username,
                        "inscription.duplicated.exception");
            } else {
                Courses courses = courseRepository.findById(courseId);
                inscriptions = new Inscriptions(user, courses, false);
                user.getInscriptions().add(inscriptions);
                for (Modules module : courses.getModules()) {
                    ModuleUser moduleUser = new ModuleUser(user, module, null);
                    for (Content content : module.getContents()) {
                        ContentUser contentUser = new ContentUser(user, content, null);
                        contentUserRepository.create(contentUser);
                    }
                    moduleUserRepository.create(moduleUser);
                }
            }

        }
    }

    @Transactional
    public void remove(String username, Long courseId) throws InstanceNotFoundException {
        Inscriptions inscriptions = inscriptionsRepository.findInscription(courseId, username);
        if (inscriptions == null) {
            throw new InstanceNotFoundException(courseId.toString(), "Inscriptions", "Inscription not found");
        } else {
            inscriptionsRepository.remove(inscriptions);
            contentUserImpl.remove(courseId,username);
            moduleUserImpl.remove(courseId,username);
        }
    }

    @Transactional(readOnly = true)
    public List<Courses> findCoursesInscriptionsByUser(String username) {
        return inscriptionsRepository.findCoursesByUser(username);
    }

    @Transactional(readOnly = true)
    public Inscriptions findInscription(long courseId, String username) {
        return inscriptionsRepository.findInscription(courseId, username);
    }

    @Transactional
    public void updateInscriptionStatus(long courseId, String username) throws InstanceNotFoundException {

        Users user = userRepository.findByUsername(username);
        Inscriptions inscriptions = inscriptionsRepository.findInscription(courseId, username);
        if (inscriptions == null) {
            throw new InstanceNotFoundException(String.valueOf(courseId), Inscriptions.class.toString(), "Inscription not found");
        } else {
            List<ModuleUser> modules = moduleUserRepository.findListModuleUser(user.getUserId(), courseId);
            for (ModuleUser module : modules) {
                Boolean moduleCompleted = module.getCompleted();
                Boolean inscriptionCompleted = inscriptions.isCompleted();
                if ((moduleCompleted == null || !moduleCompleted) && (inscriptionCompleted != null && inscriptionCompleted)) {
                    inscriptions.setCompleted(false);
                    inscriptionsRepository.update(inscriptions);
                } else if ((moduleCompleted != null && moduleCompleted) && (inscriptionCompleted == null || !inscriptionCompleted)) {
                    inscriptions.setCompleted(true);
                    inscriptionsRepository.update(inscriptions);
                }
            }
        }
    }


}
