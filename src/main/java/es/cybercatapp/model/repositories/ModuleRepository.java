package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.entities.ModuleId;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ModuleRepository extends AbstractRepository<Module> {

    private static final String UPDATE_MODULE_NAME = "UPDATE Module m SET m.id.moduleName = :newModuleName WHERE m.id.moduleName = :oldModuleName AND m.course.courseId = :courseId";
    private static final String UPDATE_CONTENT_QUERY = "UPDATE Content c SET c.module.moduleName = :newModuleName WHERE c.module.moduleName = :oldModuleName AND c.module.course.courseId = :courseId";


    public Module findByModuleId(ModuleId moduleId) throws InstanceNotFoundException {
        try {
            Module t = entityManager.find(Module.class, moduleId);
            if (t == null) {
                throw new NoResultException(moduleId.toString());
            }
            return t;
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public void updateModuleName(Long courseId, String newModuleName, String oldModuleName) {
        ModuleId moduleId = new ModuleId(oldModuleName, courseId);

        try {
            Module module = findByModuleId(moduleId);
            for (Content content : module.getContents()) {
                Query query = entityManager.createQuery(UPDATE_CONTENT_QUERY);
                query.setParameter("courseId", courseId);
                query.setParameter("newModuleName", newModuleName);
                query.setParameter("oldModuleName", oldModuleName);
            }

            Query query = entityManager.createQuery(UPDATE_MODULE_NAME);
            query.setParameter("courseId", courseId);
            query.setParameter("newModuleName", newModuleName);
            query.setParameter("oldModuleName", oldModuleName);
            int updatedMRows = query.executeUpdate();

            logger.info("Number of updated rows in Module: " + updatedMRows);
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
        } catch (InstanceNotFoundException e) {
            throw new NoResultException(moduleId.toString());
        }
    }

}
