package es.cybercatapp.model.impl;


import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    private MessageSource messageSource;

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
            Module module = new Module(modulename, LocalDate.now(), courses);
            moduleRepository.create(module);
            return module;

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(courseId, Courses.class.toString(), "Course not found");
        }
    }

    @Transactional
    public List<Module> findModulesByCourse(Long courseId) throws InstanceNotFoundException {
        try {
            courseRepository.findById(courseId);
        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(courseId, Module.class.toString(), "Course not found");
        }
        return moduleRepository.findModulesByCurses(courseId);
    }

    @Transactional
    public void remove(Long moduleId) throws InstanceNotFoundException {

        try {
            Module module = moduleRepository.findById(moduleId);
            moduleRepository.remove(module);

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId, Module.class.toString(), "Module not found");
        }
    }

    @Transactional
    public void update(Long moduleId, String modulename) throws InstanceNotFoundException, DuplicatedResourceException {

        try {
            Module module = moduleRepository.findById(moduleId);
            if (modulename.equals(module.getModule_name())) {
                throw exceptionGenerationUtils.toDuplicatedResourceException("Module", moduleId.toString(),
                        "updatemodule.duplicated.exception");
            } else {
                module.setModule_name(modulename);
                moduleRepository.update(module);
            }

        } catch (InstanceNotFoundException ex) {
            throw new InstanceNotFoundException(moduleId, Module.class.toString(), "Module not found");


        }
    }
}
