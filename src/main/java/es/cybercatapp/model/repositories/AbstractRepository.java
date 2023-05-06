package es.cybercatapp.model.repositories;

import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import es.cybercatapp.common.Constants;
import org.hibernate.Hibernate;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.List;

public abstract class AbstractRepository<T> {

    protected final Logger logger;

    
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
            throw exceptionGenerationUtils.toInstanceNotFoundException(id.toString(), genericType.getSimpleName(),
                    Constants.INSTANCE_NOT_FOUND_MESSAGE);
        }
    }





}
