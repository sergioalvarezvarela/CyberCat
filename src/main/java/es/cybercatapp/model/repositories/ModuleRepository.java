package es.cybercatapp.model.repositories;

import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Module;
import es.cybercatapp.model.entities.ModuleId;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.MessageFormat;

@Repository
public class ModuleRepository extends AbstractRepository<Module> {

    private static final String UPDATE_MODULE_NAME = "UPDATE Module m SET m.id.moduleName = :newModuleName WHERE m.id.moduleName = :oldModuleName AND m.course.courseId = :courseId";


    public Module findByModuleId(ModuleId moduleId) throws InstanceNotFoundException {
        try {
            Module t = entityManager.find(Module.class, moduleId);
            if (t == null) {
                throw new NoResultException(moduleId.toString());
            }
            return t;
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            throw exceptionGenerationUtils.toInstanceNotFoundException(
                    moduleId.toString(),
                    Module.class.getSimpleName(),
                    Constants.INSTANCE_NOT_FOUND_MESSAGE
            );
        }
    }

    public void updateModuleName(Long courseId, String newModuleName, String oldModuleName) {
        try {
            Query query = entityManager.createQuery(UPDATE_MODULE_NAME);
            query.setParameter("courseId", courseId);
            query.setParameter("newModuleName", newModuleName);
            query.setParameter("oldModuleName", oldModuleName);
            int updatedRows = query.executeUpdate();
            logger.info("Number of updated rows: " + updatedRows);
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
        }
    }


}
