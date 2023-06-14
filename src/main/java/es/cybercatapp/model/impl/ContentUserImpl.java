package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.ContentUser;
import es.cybercatapp.model.entities.Modules;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.ContentRepository;
import es.cybercatapp.model.repositories.ContentUserRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "contentUserImpl")
public class ContentUserImpl {


    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    ContentUserRepository contentUserRepository;

    @Transactional(readOnly = true)
    public List<ContentUser> findListContentUser(String username, Long moduleId) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        return contentUserRepository.findListContentUser(user.getUserId(), moduleId);
    }

    @Transactional
    public void updateContentInscription(String username, long moduleId) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        Modules modules = moduleRepository.findById(moduleId);
        List<Content> contents = contentUserRepository.findListContent(user.getUserId(), moduleId);
        for (Content content : modules.getContents()) {
            if (!contents.contains(content)) {
                ContentUser cU = new ContentUser(user, content, null);
                contentUserRepository.create(cU);
            }
        }
    }

    @Transactional
    public void updateUserContent(String username, long contentId, boolean completed) throws InstanceNotFoundException {
        Users user = userRepository.findByUsername(username);
        Content content = contentRepository.findById(contentId);
        ContentUser contentUser = new ContentUser(user, content, completed);
        contentUserRepository.update(contentUser);
    }


}
