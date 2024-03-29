package es.cybercatapp.model.services;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.*;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    ModuleUserRepository moduleUserRepository;

    @Autowired
    ContentUserRepository contentUserRepository;

    @Transactional
    public Modules create(String modulename, Long courseId) throws InstanceNotFoundException, DuplicatedResourceException {

            Modules modules = moduleRepository.findModulesByModuleNameAndCourse(courseId, modulename);
            if (modules != null) {

                throw exceptionGenerationUtils.toDuplicatedResourceException("Module", modulename,
                        "createmodule.duplicated.exception");
            } else {
                Courses courses = courseRepository.findById(courseId);
                modules = new Modules(modulename, LocalDate.now(), courses, courses.getModules().size() + 1);
                modules.setModuleName(modulename);
                modules.setCourseId(courses);
                modules = moduleRepository.create(modules);
                courses.getModules().add(modules);
                courseRepository.update(courses);
                return modules;
            }

    }

    @Transactional(readOnly = true)
    public Modules findModulesById(long id) throws InstanceNotFoundException {
        return moduleRepository.findById(id);
    }




    @Transactional
    public void remove(Long moduleId) throws InstanceNotFoundException {

        Modules modules = moduleRepository.findById(moduleId);
        moduleRepository.remove(modules);


    }

    @Transactional
    public void updatePositions(Modules modules) {
        moduleRepository.update(modules);
    }

    @Transactional
    public void update(Long courseId, Long moduleId, String newModuleName) throws
            DuplicatedResourceException, InstanceNotFoundException {

        Modules modules = moduleRepository.findModulesByModuleNameAndCourse(courseId, newModuleName);
        Modules modules1 = moduleRepository.findById(moduleId);
        if (modules != null) {

            throw exceptionGenerationUtils.toDuplicatedResourceException("Module", moduleId.toString(),
                    "updatemodule.duplicated.exception");
        } else {
            modules1.setModuleName(newModuleName);
            moduleRepository.update(modules1);
        }


    }
}
