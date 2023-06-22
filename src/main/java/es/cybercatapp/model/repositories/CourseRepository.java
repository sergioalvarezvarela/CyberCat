package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Modules;
import es.cybercatapp.model.entities.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Repository
public class CourseRepository extends AbstractRepository<Courses> {

    private static final String FIND_ALL_COURSES_BY_PRICES_DESC = "SELECT t FROM Courses t where t.course_name LIKE :coursename order by t.course_price DESC";

    private static final String FIND_ALL_COURSES_BY_PRICES_ASC = "SELECT t FROM Courses t where t.course_name LIKE :coursename order by t.course_price ASC";

    private static final String FIND_ALL_COURSES_BY_PRICES_ASC_CAT = "SELECT t FROM Courses t where t.course_name LIKE :coursename AND t.course_category = :category order by t.course_price ASC";

    private static final String FIND_ALL_COURSES_BY_PRICES_DESC_CAT = "SELECT t FROM Courses t where t.course_name LIKE :coursename AND t.course_category = :category  order by t.course_price DESC";

    private static final String FIND_ALL_QUERY = "SELECT t FROM Courses t where t.course_name LIKE :coursename";

    private static final String FIND_ALL_QUERY_CAT = "SELECT t FROM Courses t where t.course_name LIKE :coursename AND t.course_category = :category";

    private static final String FIND_ALL_COURSES_BY_GRADE = "SELECT t FROM Courses t where t.course_name LIKE :coursename ORDER BY t.grade DESC";

    private static final String FIND_ALL_COURSES_BY_GRADE_CAT = "SELECT t FROM Courses t where t.course_name LIKE :coursename AND t.course_category = :category  order by t.grade DESC";


    private static final String INICIALIZAR_MODULOS = "SELECT c FROM Courses c LEFT JOIN FETCH c.modules WHERE c.courseId = :courseId";

    public List<Courses> findAllFiltered(int pageIndex, int filter, String category, String word) {
        TypedQuery<Courses> query = null;

        try {
            if (category.equals("Todos")) {
                if (filter == 1) {
                    query = entityManager.createQuery(FIND_ALL_QUERY, Courses.class);
                } else if (filter == 3) {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_PRICES_DESC, Courses.class);


                } else if (filter == 2) {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_PRICES_ASC, Courses.class);
                } else {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_GRADE, Courses.class);
                }
                query.setParameter("coursename", "%" + word + "%");
                query.setFirstResult(pageIndex);
                query.setMaxResults(5);
                return query.getResultList();


            } else {
                if (filter == 1) {
                    query = entityManager.createQuery(FIND_ALL_QUERY_CAT, Courses.class);
                } else if (filter == 3) {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_PRICES_DESC_CAT, Courses.class);
                } else if (filter == 2) {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_PRICES_ASC_CAT, Courses.class);
                } else {
                    query = entityManager.createQuery(FIND_ALL_COURSES_BY_GRADE_CAT, Courses.class);
                }
            }
            query.setParameter("coursename", "%" + word + "%");
            query.setParameter("category", Category.valueOf(category.toUpperCase()));
            query.setFirstResult(pageIndex);
            query.setMaxResults(5);
            return query.getResultList();
        } catch (NoResultException e) {

            logger.error(e.getMessage(), e);
            return Collections.emptyList();

        }
    }

    public Courses inicializarModulos(Courses courses) {
        try {
            TypedQuery<Courses> query = entityManager.createQuery(INICIALIZAR_MODULOS, Courses.class);
            query.setParameter("courseId", courses.getCourseId());
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
