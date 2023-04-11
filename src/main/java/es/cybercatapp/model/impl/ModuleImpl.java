package es.cybercatapp.model.impl;


import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.entities.ModuleId;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service(value = "moduleImpl")
public class ModuleImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Transactional
    public Module create(String modulename, Long courseId) throws InstanceNotFoundException {

        try {
            Courses courses = courseRepository.findById(courseId);
            Module module = new Module(modulename, LocalDate.now(), courses.getModules().size() + 1, courses);
            module.getId().setModuleName(modulename);
            module.getId().setCourseId(courseId);
            module = moduleRepository.create(module);
            courses.getModules().add(module);
            courseRepository.update(courses);
            return module;

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(courseId.toString(), Courses.class.toString(), "Course not found");
        }
    }


    @Transactional
    public void remove(Long courseId, String moduleName) throws InstanceNotFoundException {
        ModuleId moduleId = new ModuleId(moduleName, courseId);

        Module module = moduleRepository.findByModuleId(moduleId);
        if (module == null) {
            throw new InstanceNotFoundException(moduleId.toString(), Module.class.toString(), "Module not found");
        }
        moduleRepository.remove(module);


    }

    @Transactional
    public void update(Long courseId, String moduleName, String newModuleName) throws
            DuplicatedResourceException, InstanceNotFoundException {

        ModuleId moduleId = new ModuleId(moduleName, courseId);
        ModuleId newModuleId = new ModuleId(newModuleName, courseId);
        if (moduleRepository.findByModuleId(newModuleId) != null) {

            throw exceptionGenerationUtils.toDuplicatedResourceException("Module", moduleId.toString(),
                    "updatemodule.duplicated.exception");
        } else {
            moduleRepository.updateModuleName(courseId, newModuleName, moduleName);
        }


    }
}
