package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class ContentRepository extends AbstractRepository<Content> {

    private static final String FIND_CONTENT_NAME_BY_MODULE = "SELECT c FROM Contents c WHERE c.contentName = :contentName AND c.module.moduleId = :moduleId";

    private static final String FIND_CONTENT_BY_MODULE_AND_POSITION= "SELECT c FROM Contents c WHERE c.module.moduleId = :moduleId AND c.contentPosition = :contentPosition";

    public Content findContentsByContentNameAndModule(Long moduleId, String contentName) {
        try {
            TypedQuery<Content> query = entityManager.createQuery(FIND_CONTENT_NAME_BY_MODULE, Content.class);
            query.setParameter("moduleId", moduleId);
            query.setParameter("contentName", contentName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }

    public Content findContentByModuleIdAndPosition(Long moduleId, int pos) {
        try {
            TypedQuery<Content> query = entityManager.createQuery(FIND_CONTENT_BY_MODULE_AND_POSITION, Content.class);
            query.setParameter("moduleId", moduleId);
            query.setParameter("contentPosition", pos);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }




}
