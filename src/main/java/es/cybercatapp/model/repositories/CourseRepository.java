package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Repository
public class CourseRepository extends AbstractRepository<Courses> {
}
