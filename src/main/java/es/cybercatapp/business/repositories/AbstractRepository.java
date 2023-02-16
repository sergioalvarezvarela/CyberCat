package es.cybercatapp.business.repositories;

import es.cybercatapp.business.exceptions.InstanceNotFoundException;
import es.cybercatapp.business.utils.ExceptionGenerationUtils;
import es.cybercatapp.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;

public abstract class AbstractRepository<T> {

    protected final Logger logger;
    
    private static final String FIND_ALL_QUERY = "SELECT t FROM {0} t";
    private static final String FIND_ALL_ORDERED_QUERY = "SELECT t FROM {0} t ORDER BY t.{1}";
    private static final String FIND_BY_TEXT_ATTRIBUTE_QUERY = "SELECT t FROM {0} t WHERE t.{1} = ''{2}'' ORDER BY t.{3}";
    
    private final Class<T> genericType;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;
    
    public AbstractRepository() {
        this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractRepository.class);
        this.logger = LoggerFactory.getLogger(this.genericType);
    }

    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }
    
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    public T findById(Long id) throws InstanceNotFoundException {
        try{
            T t = entityManager.find(genericType, id);
            if(t == null) {
                throw new NoResultException(Long.toString(id));
            }
            return t;
        } catch(NoResultException e) {
            logger.error(e.getMessage(), e);
            throw exceptionGenerationUtils.toInstanceNotFoundException(id, genericType.getSimpleName(), 
                    Constants.INSTANCE_NOT_FOUND_MESSAGE);
        }
    }
    
    public List<T> findAll() {
        Query query = entityManager.createQuery(MessageFormat.format(FIND_ALL_QUERY, 
                genericType.getSimpleName()));
        return query.getResultList();
    }
    
    public List<T> findAll(String orderColumn) {
        Query query = entityManager.createQuery(MessageFormat.format(FIND_ALL_ORDERED_QUERY, 
                genericType.getSimpleName(), orderColumn));
        return query.getResultList();
    }
    
    public List<T> findByStringAttribute(String attribute, String value, String orderColumn) {
        Query query = entityManager.createQuery(MessageFormat.format(FIND_BY_TEXT_ATTRIBUTE_QUERY, 
                genericType.getSimpleName(), attribute, value, orderColumn));
        return query.getResultList();
    }
}
