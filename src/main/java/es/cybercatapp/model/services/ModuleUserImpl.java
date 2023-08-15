package es.cybercatapp.model.services;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.*;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "moduleUserImpl")
public class ModuleUserImpl {
    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ModuleUserRepository moduleUserRepository;

    @Autowired
    ContentUserRepository contentUserRepository;

    @Autowired
    ContentUserImpl contentUserImpl;

    @Transactional(readOnly = true)
    public List<ModuleUser> findListModuleUser(String username, long courseId) {
        Users user = userRepository.findByUsername(username);
        return moduleUserRepository.findListModuleUser(user.getUserId(), courseId);
    }

    @Transactional
    public void updateModuleInscription(String username, long courseId) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        Courses courses = courseRepository.findById(courseId);
        List<ModuleUser> moduleUsers = moduleUserRepository.findListModuleUser(user.getUserId(), courseId);

        for (Modules module : courses.getModules()) {
            ModuleUser moduleUser = findModuleUserInList(moduleUsers, module);

            if (moduleUser == null) {
                moduleUser = new ModuleUser(user, module, null);
                moduleUserRepository.create(moduleUser);
            }

            updateModuleUserCompletionStatus(user, module, moduleUser);
        }
    }

    private ModuleUser findModuleUserInList(List<ModuleUser> moduleUsers, Modules module) {
        for (ModuleUser moduleUser : moduleUsers) {
            if (moduleUser.getModule().equals(module)) {
                return moduleUser;
            }
        }

        return null;
    }

    private void updateModuleUserCompletionStatus(Users user, Modules module, ModuleUser moduleUser) {
        List<ContentUser> contentUsers = contentUserRepository.findListContentUser(user.getUserId(), module.getModuleId());
        Boolean completionStatus = null;

        for (ContentUser contentUser : contentUsers) {
            Boolean contentUserCompleted = contentUser.getCompleted();

            if (contentUserCompleted == null) {
                completionStatus = null;
                break;
            } else if (!contentUserCompleted) {
                completionStatus = false;
                break;
            } else {
                completionStatus = true;
            }
        }

        if (completionStatus != moduleUser.getCompleted()) {
            moduleUser.setCompleted(completionStatus);
            moduleUserRepository.update(moduleUser);
        }
    }


    @Transactional
    public void remove(long courseId, String username) throws InstanceNotFoundException {
        Users users = userRepository.findByUsername(username);
        List<ModuleUser> moduleUsers = moduleUserRepository.findListModuleUser(users.getUserId(), courseId);
        for (ModuleUser moduleUser : moduleUsers) {
            moduleUserRepository.remove(moduleUser);
            contentUserImpl.remove(moduleUser.getModule().getModuleId(),users.getUsername());
        }
    }
}


