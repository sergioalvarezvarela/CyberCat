package es.cybercatapp.model.impl;

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

    @Transactional(readOnly = true)
    public List<ModuleUser> findListModuleUser(String username, long courseId) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        return moduleUserRepository.findListModuleUser(user.getUserId(), courseId);
    }

    @Transactional
    public void updateModuleInscription(String username, long courseId) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        Courses courses = courseRepository.findById(courseId);
        List<Modules> modules = moduleUserRepository.findListModule(user.getUserId(), courseId);
        List<ModuleUser> moduleUsers = moduleUserRepository.findListModuleUser(user.getUserId(), courseId);
        int i = 0;

        for (Modules module : courses.getModules()) {
            if (!modules.contains(module)) {
                ModuleUser mU = new ModuleUser(user, module, null);
                moduleUserRepository.create(mU);
            } else {
                List<ContentUser> contentUsers = contentUserRepository.findListContentUser(user.getUserId(), module.getModuleId());

                Boolean allContentUsersNull = true;
                Boolean allContentUsersTrue = true;

                if (contentUsers.isEmpty()) {
                    if (moduleUsers.get(i).getCompleted() == null || !moduleUsers.get(i).getCompleted()) {
                        moduleUsers.get(i).setCompleted(true);
                        moduleUserRepository.update(moduleUsers.get(i));
                    }
                } else {
                    for (ContentUser contentUser : contentUsers) {
                        Boolean contentUserCompleted = contentUser.getCompleted();
                        if (contentUserCompleted == null) {
                            allContentUsersTrue = false;
                        } else if (!contentUserCompleted) {
                            allContentUsersTrue = false;
                            allContentUsersNull = false;
                            moduleUsers.get(i).setCompleted(false);
                            moduleUserRepository.update(moduleUsers.get(i));
                            break;
                        } else {
                            allContentUsersNull = false;
                        }
                    }

                    if (allContentUsersTrue && (moduleUsers.get(i).getCompleted() == null || !moduleUsers.get(i).getCompleted())) {
                        moduleUsers.get(i).setCompleted(true);
                        moduleUserRepository.update(moduleUsers.get(i));
                    } else if (allContentUsersNull && moduleUsers.get(i).getCompleted() != null) {
                        moduleUsers.get(i).setCompleted(null);
                        moduleUserRepository.update(moduleUsers.get(i));
                    }
                }
            }
            i++;
        }
    }

    @Transactional
    public void remove(long courseId, String username) throws InstanceNotFoundException {
        Users users = userRepository.findByUsername(username);
        List<ModuleUser> moduleUsers = moduleUserRepository.findListModuleUser(users.getUserId(), courseId);
        for (ModuleUser moduleUser : moduleUsers) {
            moduleUserRepository.remove(moduleUser);
        }
    }
}


