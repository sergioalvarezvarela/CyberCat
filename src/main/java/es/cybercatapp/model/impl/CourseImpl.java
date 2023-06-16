package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.InscriptionsRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service(value = "courseImpl")
public class CourseImpl {

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
    private DiplomaImpl diplomaImpl;

    @Autowired
    private InscriptionsRepository inscriptionsRepository;

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
        Courses courses = courseRepository.create(new Courses(coursename, description, LocalDate.now(), image, Category.valueOf(category), price,0,0,0, user));
        saveCourseImage(courses.getCourseId(), image, imageContents);
        return courses;
    }


    @Transactional(readOnly = true)
    public Courses findCoursesById(long id) throws InstanceNotFoundException {
        return courseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Courses> findAllFiltered(int start, int filter, String category, String word) throws InstanceNotFoundException {
        return courseRepository.findAllFiltered(start, filter, category, word);


    }




    @Transactional
    public void updatePositions(Courses courses) {
        courseRepository.update(courses);
    }


    @Transactional
    public void update(long id, String coursename, float price, String category,
                       String image, byte[] imageContents, String description, String userowner) throws InstanceNotFoundException, DuplicatedResourceException {
        Courses course = courseRepository.findById(id);
        Users user = userRepository.findByUsername(userowner);
        if (course == null) {
            throw new InstanceNotFoundException(String.valueOf(id), Courses.class.toString(), "Course not found");
        } else {
            Courses newcourse = new Courses(coursename, description, course.getCreation_date(), course.getCourse_photo(), Category.valueOf(category), price,course.getTotal_comments(),course.getPuntuation(),course.getGrade(), user);
            newcourse.setCourseId(course.getCourseId());
            newcourse.setModules(course.getModules());
            if (image != null && image.trim().length() > 0 && imageContents != null) {
                try {
                    deleteCourseImage(id, user.getImagen_perfil());
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
                saveCourseImage(id, image, imageContents);
                newcourse.setCourse_photo(image);
                course.setCourse_photo(image);
            }
            if (!course.equals(newcourse)) {
                course.setCourse_name(coursename);
                course.setCourse_price(price);
                course.setCourse_category(Category.valueOf(category));
                course.setCourse_description(description);
                courseRepository.update(course);
            } else {
                throw exceptionGenerationUtils.toDuplicatedResourceException("Course", course.getCourseId().toString(),
                        "updatecourse.duplicated.exception");
            }
        }
    }



    @Transactional
    public void remove(long id) throws InstanceNotFoundException, IOException {
        Courses course = courseRepository.findById(id);
        if (course == null) {
            throw new InstanceNotFoundException(String.valueOf(id), Courses.class.toString(), "Course not found");
        } else {
            courseRepository.remove(course);
            deleteCourseImage(id,course.getCourse_photo());
            diplomaImpl.deletePdfByPrefix(String.valueOf(id));
        }

    }

    @Transactional(readOnly = true)
    public List<Courses> findCoursesMoreInscriptions()  {
         return inscriptionsRepository.findCoursesByCountUsers();
    }

    private void saveCourseImage(Long id, String image, byte[] imageContents) {
        if (image != null && image.trim().length() > 0 && imageContents != null) {
            File courseDir = new File(resourcesDir, id.toString());
            courseDir.mkdirs();
            File profilePicture = new File(courseDir, image);
            try (FileOutputStream outputStream = new FileOutputStream(profilePicture);) {
                IOUtils.copy(new ByteArrayInputStream(imageContents), outputStream);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Transactional
    public byte[] getImage(Long id) throws InstanceNotFoundException {
        Courses courses = courseRepository.findById(id);
        try {
            return getImageCourse(id, courses.getCourse_photo());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    private byte[] getImageCourse(Long id, String image) throws IOException {
        if (image != null && image.trim().length() > 0) {
            File courseDir = new File(resourcesDir, id.toString());
            File coursePicture = new File(courseDir, image);
            try (FileInputStream input = new FileInputStream(coursePicture)) {
                return IOUtils.toByteArray(input);
            }
        }
        return null;
    }

    private void deleteCourseImage(Long id, String image) throws IOException {
        if (image != null && image.trim().length() > 0) {
            File courseDir = new File(resourcesDir, id.toString());
            File coursePicture = new File(courseDir, image);
            Files.delete(coursePicture.toPath());
        }
    }

}
