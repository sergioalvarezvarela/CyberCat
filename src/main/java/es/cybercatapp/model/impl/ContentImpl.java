package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Modules;
import es.cybercatapp.model.entities.StringContent;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.ContentRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "contentImpl")
public class ContentImpl {


    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ContentRepository contentRepository;

    @Transactional
    public StringContent createTeoricContent(Long moduleId, String contentName, String html) throws InstanceNotFoundException, DuplicatedResourceException {
        try {

            Modules module = moduleRepository.findById(moduleId);
            StringContent content = (StringContent) contentRepository.findContentsByContentNameAndModule(moduleId, contentName);
            if (content != null) {

                throw exceptionGenerationUtils.toDuplicatedResourceException("Content", contentName,
                        "createcontent.duplicated.exception");
            } else {
                content = new StringContent(contentName, module.getContents().size() + 1, module, html);
                contentRepository.create(content);
                module.getContents().add(content);
                moduleRepository.update(module);
                return content;
            }

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId.toString(), Modules.class.toString(), "Module not found");
        }
    }

    @Transactional
    public void remove(Long contentId) throws InstanceNotFoundException {

        Content content = contentRepository.findById(contentId);
        if (content == null) {
            throw new InstanceNotFoundException(contentId.toString(), Content.class.toString(), "Content not found");
        }
        contentRepository.remove(content);


    }


}
