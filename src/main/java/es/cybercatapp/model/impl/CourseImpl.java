package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
public class CourseImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    private UserRepository userRepository;

    private File resourcesDir;

    @PostConstruct
    public void init() {
        resourcesDir = new File(configurationParameters.getResources());
    }


    @Transactional
    public Courses create(String coursename, float price, String category,
                          String image, byte[] imageContents, String description, String userowner) {
        Users user = userRepository.findByUsername(userowner);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", userowner));
        }
        Courses courses = courseRepository.create(new Courses(coursename, description, LocalDate.now(), image, user, Category.valueOf(category), price));
        saveCourseImage(courses.getCourseId(), image, imageContents);
        return courses;
    }

    @Transactional
    public List<Courses> findCoursesByOwner(String owner){
        Users user = userRepository.findByUsername(owner);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", owner));
        }
        return courseRepository.findCoursesByUserOwner(user.getUserId());
    }


    @Transactional
    public void Remove(long id) throws InstanceNotFoundException {
        Courses course = courseRepository.findById(id);
        if (course== null) {
            throw new InstanceNotFoundException(id,"Course","Course not found");
        }
        courseRepository.remove(course);
    }
    private void saveCourseImage(Long id, String image, byte[] imageContents) {
        if (image != null && image.trim().length() > 0 && imageContents != null) {
            File userDir = new File(resourcesDir, id.toString());
            userDir.mkdirs();
            File profilePicture = new File(userDir, image);
            try (FileOutputStream outputStream = new FileOutputStream(profilePicture);) {
                IOUtils.copy(new ByteArrayInputStream(imageContents), outputStream);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
