package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Modules;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class ModuleRepository extends AbstractRepository<Modules> {

    private static final String FIND_COURSES_BY_MODULE_BY_NAME_AND_COURSE = "SELECT m FROM Module m WHERE m.moduleName = :moduleName AND m.courseId.courseId = :courseId";

    public Modules findModulesByModuleNameAndCourse(Long courseId, String moduleName) {
        try {
            TypedQuery<Modules> query = entityManager.createQuery(FIND_COURSES_BY_MODULE_BY_NAME_AND_COURSE, Modules.class);
            query.setParameter("courseId", courseId);
            query.setParameter("moduleName", moduleName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }

}
