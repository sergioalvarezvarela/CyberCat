package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Comment;
import es.cybercatapp.model.entities.Content;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentRepository extends AbstractRepository<Comment> {

    private static final String FIND_COMMENT_BY_COURSE_AND_USER = "SELECT c FROM Comment c WHERE c.courses.courseId = :courseId AND c.users.username = :username";

    private static final String FIND_COMMENT_BY_COURSE = "SELECT c FROM Comment c WHERE c.courses.courseId = :courseId";

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

    public List<Comment> findCommentByCourse(Long courseId, int start) {
        try {
            TypedQuery<Comment> query = entityManager.createQuery(FIND_COMMENT_BY_COURSE, Comment.class);
            query.setParameter("courseId", courseId);
            query.setFirstResult(start);
            query.setMaxResults(5);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }
}
