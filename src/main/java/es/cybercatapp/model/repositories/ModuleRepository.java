package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.ModuleUser;
import es.cybercatapp.model.entities.Modules;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Repository
public class ModuleRepository extends AbstractRepository<Modules> {

    private static final String FIND_COURSES_BY_MODULE_BY_NAME_AND_COURSE = "SELECT m FROM Module m WHERE m.moduleName = :moduleName AND m.courseId.courseId = :courseId";

    private static final String INICIALIZAR_CONTENTIDO = "SELECT m FROM Module m LEFT JOIN FETCH m.contents WHERE m.moduleId = :moduleId";

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



    public Modules inicializarContenidos(Modules modules) {
        try {
            TypedQuery<Modules> query = entityManager.createQuery(INICIALIZAR_CONTENTIDO, Modules.class);
            query.setParameter("moduleId", modules.getModuleId());
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


}
