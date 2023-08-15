package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.ModuleUser;
import es.cybercatapp.model.entities.Modules;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ModuleUserRepository extends AbstractRepository<ModuleUser> {

    private static final String LIST_MODULE_USER = "SELECT mu FROM ModuleUser mu" +
            " JOIN FETCH mu.module m" +
            " JOIN FETCH m.courseId c" +
            " JOIN FETCH mu.users u" +
            " WHERE c.courseId = :courseId" +
            " AND u.userId = :userId order by m.modulePosition";

    private static final String LIST_MODULES = "SELECT m FROM ModuleUser mu" +
            " JOIN mu.module m" +
            " JOIN m.courseId c" +
            " JOIN mu.users u" +
            " WHERE c.courseId = :courseId" +
            " AND u.userId = :userId" +
            " ORDER BY m.modulePosition";


    public List<ModuleUser> findListModuleUser(Long userId, Long courseId) {
        try {
            TypedQuery<ModuleUser> query = entityManager.createQuery(LIST_MODULE_USER, ModuleUser.class);
            query.setParameter("courseId", courseId);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }


}
