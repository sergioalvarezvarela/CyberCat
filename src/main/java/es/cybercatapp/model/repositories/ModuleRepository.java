package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Module;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ModuleRepository extends AbstractRepository<Module>{
    private static final String FIND_MODULES_BY_COURSE_QUERY = "SELECT c FROM Module c WHERE c.courses.id = :courseId";

    public List<Module> findModulesByCurses(Long courseId) {
        TypedQuery<Module> query = entityManager.createQuery(FIND_MODULES_BY_COURSE_QUERY, Module.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}
