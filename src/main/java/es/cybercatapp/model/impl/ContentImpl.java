/*package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.ContentRepository;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    public Content createStringContent(String modulename, Long courseId, String contentName, String enunciado, boolean markdown) throws InstanceNotFoundException {
            ModuleId moduleId = new ModuleId(modulename,courseId);
        try {
            Module module = moduleRepository.findByModuleId(moduleId);
            Content content = new StringContent(contentName,module, enunciado, markdown);
            contentRepository.create(content);
            module.getContents().add(content);
            moduleRepository.update(module);
            return content;

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(modulename, Courses.class.toString(), "Module not found");
        }
    }

}*/
