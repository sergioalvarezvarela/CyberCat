package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Comment;
import es.cybercatapp.model.entities.Content;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class CommentRepository extends AbstractRepository<Comment> {

    private static final String FIND_COMMENT_BY_COURSE_AND_USER = "SELECT c FROM Comment c WHERE c.courses.courseId = :courseId AND c.users.username = :username";


    public Comment findCommentByCourseAndUser(Long courseId, String username) {
        try {
            TypedQuery<Comment> query = entityManager.createQuery(FIND_COMMENT_BY_COURSE_AND_USER, Comment.class);
            query.setParameter("courseId", courseId);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }
}
