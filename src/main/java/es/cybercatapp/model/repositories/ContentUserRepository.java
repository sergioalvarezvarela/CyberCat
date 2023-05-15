package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.ContentUser;
import es.cybercatapp.model.entities.ModuleUser;
import es.cybercatapp.model.entities.Modules;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ContentUserRepository extends AbstractRepository<ContentUser> {

    private static final String LIST_CONTENT_USER = "SELECT cu FROM ContentUser cu" +
            " JOIN FETCH cu.content c" +
            " JOIN FETCH c.module m" +
            " JOIN FETCH cu.users u" +
            " WHERE m.moduleId = :moduleId" +
            " AND u.userId = :userId order by c.contentPosition";

    private static final String LIST_CONTENT = "SELECT c FROM ContentUser cu" +
            " JOIN cu.content c" +
            " JOIN c.module m" +
            " JOIN cu.users u" +
            " WHERE m.moduleId = :moduleId" +
            " AND u.userId = :userId" +
            " ORDER BY c.contentPosition";


    public List<ContentUser> findListContentUser(Long userId, Long moduleId) {
        try {
            TypedQuery<ContentUser> query = entityManager.createQuery(LIST_CONTENT_USER, ContentUser.class);
            query.setParameter("moduleId", moduleId);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }

    public List<Content> findListContent(Long userId, Long moduleId) {
        try {
            TypedQuery<Content> query = entityManager.createQuery(LIST_CONTENT, Content.class);
            query.setParameter("moduleId", moduleId);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
