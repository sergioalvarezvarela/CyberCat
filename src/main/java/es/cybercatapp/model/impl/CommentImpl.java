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
        return commentRepository.findCommentByCourseAndUser(courseId, username);
    }

    @Transactional
    public Comment create(Long courseId, String username, String description, int grade, String comment) throws DuplicatedResourceException, InstanceNotFoundException {
        Comment commentary = commentRepository.findCommentByCourseAndUser(courseId, username);
        Users user = userRepository.findByUsername(username);
        Courses courses = courseRepository.findById(courseId);

        if (commentary != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Comment", commentary.getCommentId().toString(),
                    "addcomment.duplicated.exception");
        } else {
            commentary = new Comment(description, LocalDate.now(), grade, comment, courses, user);
            commentRepository.create(commentary);
            courses.setTotal_comments(courses.getTotal_comments() + 1);
            courses.setPuntuation(courses.getPuntuation() + grade);
            courses.setGrade(((float) commentary.getCourses().getPuntuation() / (float) commentary.getCourses().getTotal_comments()));
        }
        return commentary;
    }

    @Transactional
    public void remove(Long commentId) throws InstanceNotFoundException {
        Comment commentary = commentRepository.findById(commentId);
        if (commentary == null) {
            throw new InstanceNotFoundException(commentId.toString(), Comment.class.toString(), "Comment not found");
        }
        commentary.getCourses().setPuntuation((commentary.getCourses().getPuntuation() - commentary.getGrade()));
        commentary.getCourses().setTotal_comments(commentary.getCourses().getTotal_comments() - 1);
        if (commentary.getCourses().getTotal_comments() != 0) {
            commentary.getCourses().setGrade(((float) commentary.getCourses().getPuntuation() / (float) commentary.getCourses().getTotal_comments()));
        } else {
            commentary.getCourses().setGrade(0);
        }
        courseRepository.update(commentary.getCourses());
        commentRepository.remove(commentary);
    }

    @Transactional
    public void update(Long commentId, String description, int grade, String comment) throws InstanceNotFoundException, DuplicatedResourceException {
        Comment commentary = commentRepository.findById(commentId);
        if (commentary == null) {
            throw new InstanceNotFoundException(commentId.toString(), Comment.class.toString(), "Comment not found");
        }
        if (commentary.getDescription().equals(description) && commentary.getGrade() == grade && commentary.getCommentary().equals(comment)) {
            throw exceptionGenerationUtils.toDuplicatedResourceException("Comment", commentary.getCommentId().toString(),
                    "editcomment.duplicated.exception");
        }
        if (commentary.getGrade() != grade) {
            commentary.getCourses().setPuntuation((commentary.getCourses().getPuntuation() - commentary.getGrade()));
            commentary.getCourses().setPuntuation(commentary.getCourses().getPuntuation() + grade);
            commentary.getCourses().setGrade(((float) commentary.getCourses().getPuntuation() / (float) commentary.getCourses().getTotal_comments()));
            courseRepository.update(commentary.getCourses());
        }
        commentary.setDescription(description);
        commentary.setCommentary(comment);
        commentary.setGrade(grade);
        commentRepository.update(commentary);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsbyCourse(Long courseId, int start) {
        return commentRepository.findCommentByCourse(courseId, start);
    }


}
