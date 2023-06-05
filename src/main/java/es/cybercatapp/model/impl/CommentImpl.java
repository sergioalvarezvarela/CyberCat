package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CommentRepository;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service(value = "commentImpl")
public class CommentImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;


    @Transactional(readOnly = true)
    public Comment findCommentByUserAndCourse(Long courseId, String username) {
        return commentRepository.findCommentByCourseAndUser(courseId,username);
    }

    @Transactional
    public Comment create(Long courseId,String username,String description,int grade, String comment) throws DuplicatedResourceException, InstanceNotFoundException {
        Comment commentary = commentRepository.findCommentByCourseAndUser(courseId,username);
        Users user = userRepository.findByUsername(username);
        Courses courses = courseRepository.findById(courseId);

        if (commentary != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Comment", commentary.getCommentId().toString(),
                    "addcomment.duplicated.exception");
        } else{
           commentary = new Comment(description,LocalDate.now(),grade,comment, courses, user);
           commentRepository.create(commentary);
        }
        return commentary;
    }


}
