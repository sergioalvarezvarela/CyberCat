import es.cybercatapp.Application;
import es.cybercatapp.model.entities.Category;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.*;
import es.cybercatapp.model.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseTest {
    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private CommentImpl commentImpl;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InscriptionsImpl inscriptionsImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Users getUser() throws IOException, DuplicatedResourceException {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName = image.getName();
        FileInputStream fis = new FileInputStream(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        byte[] imageBytes = bos.toByteArray();

        fis.close();
        bos.close();

        return userImpl.create(username, email, password, imageName, imageBytes);
    }

    private Courses inicializarLazy(Courses courses) {
        return courseRepository.inicializarModulos(courses);
    }

    @Test
    public void testCreateCourseAndFindCourse() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses = courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
            assertEquals(courseImpl.findCoursesById(courses.getCourseId()), courses);
            assertThrows(InstanceNotFoundException.class, () -> {
                courseImpl.findCoursesById(100L);
            });
            assertThrows(UsernameNotFound.class, () -> {
                courseImpl.create(courseName, price, category, imageName, imageBytes, description, "usernameNotFound");

            });

        } finally {
            if (courses != null) {
                courseImpl.remove(courses.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testRemove() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses = courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
            courseImpl.remove(courses.getCourseId());
            Courses finalCourses = courses;
            assertThrows(InstanceNotFoundException.class, () -> {
                courseImpl.findCoursesById(finalCourses.getCourseId());
            });
            assertThrows(InstanceNotFoundException.class, () -> {
                courseImpl.remove(100L);
            });
        } finally {
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testUpdate() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses = courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
            Courses finalCourses = courses;
            assertThrows(DuplicatedResourceException.class, () -> {
                courseImpl.update(finalCourses.getCourseId(), finalCourses.getCourse_name(), finalCourses.getCourse_price(), category, imageName, imageBytes, finalCourses.getCourse_description(), users.getUsername());
            });
            courses.setCourse_price((float) 12.00);
            courses.setCourse_name("Nuevo nombre curso");
            courses.setCourse_description("Nueva descripcion");
            courseImpl.update(courses.getCourseId(), courses.getCourse_name(), courses.getCourse_price(), category, imageName, imageBytes, courses.getCourse_description(), users.getUsername());
            assertEquals(courseImpl.findCoursesById(courses.getCourseId()), courses);
        } finally {
            if (courses != null) {
                courseImpl.remove(courses.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }


        }
    }

    @Test
    public void testGetImage() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses = courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
            assertArrayEquals(courseImpl.getImage(courses.getCourseId()), imageBytes);

        } finally {
            if (courses != null) {
                courseImpl.remove(courses.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }


        }
    }

    @Test
    public void testUpdatePositions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses = courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
            moduleImpl.create("Modulo 1", courses.getCourseId());
            moduleImpl.create("Modulo 2", courses.getCourseId());
            courses = courseImpl.findCoursesById(courses.getCourseId());
            courses = inicializarLazy(courses);
            courses.getModules().get(0).setModulePosition(1);
            courses.getModules().get(1).setModulePosition(0);
            courseImpl.updatePositions(courses);
            assertEquals(courses, courseImpl.findCoursesById(courses.getCourseId()));
        } finally {
            if (courses != null) {
                courseImpl.remove(courses.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }


        }
    }

    @Test
    public void findAllFiltered() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        String courseName1 = "Introduccion a forense";
        String courseName2 = "Forense en windows";
        String courseName3 = "Forense en Linux";
        String courseName4 = "Virus en Linux";
        String courseName5 = "Virus en Android";
        String courseName6 = "Red y seguridad";
        float price1 = (float) 10.55;
        float price2 = (float) 11.55;
        float price3 = (float) 13.55;
        float price4 = (float) 14.55;
        String category1 = String.valueOf(Category.valueOf("FORENSE"));
        String category2 = String.valueOf(Category.valueOf("RED"));
        String category3 = String.valueOf(Category.valueOf("VIRUS"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses1 = null;
        Courses courses2 = null;
        Courses courses3 = null;
        Courses courses4 = null;
        Courses courses5 = null;
        Courses courses6 = null;
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = bos.toByteArray();

            fis.close();
            bos.close();

            courses1 = courseImpl.create(courseName1, price1, category1, imageName, imageBytes, description, users.getUsername());
            courses2 = courseImpl.create(courseName2, price2, category1, imageName, imageBytes, description, users.getUsername());
            courses3 = courseImpl.create(courseName3, price3, category1, imageName, imageBytes, description, users.getUsername());
            courses4 = courseImpl.create(courseName4, price4, category3, imageName, imageBytes, description, users.getUsername());
            courses5 = courseImpl.create(courseName5, price1, category3, imageName, imageBytes, description, users.getUsername());
            courses6 = courseImpl.create(courseName6, price2, category2, imageName, imageBytes, description, users.getUsername());

            List<Courses> list1 = new ArrayList<>();
            list1.add(courses1);
            list1.add(courses2);
            list1.add(courses3);
            assertEquals(list1, courseImpl.findAllFiltered(0, 1, "FORENSE", ""));
            List<Courses> list2 = new ArrayList<>();
            list2.add(courses4);
            assertEquals(list2, courseImpl.findAllFiltered(0, 1, "VIRUS", "Linux"));
            List<Courses> list3 = new ArrayList<>();
            list3.add(courses1);
            list3.add(courses5);
            list3.add(courses2);
            list3.add(courses6);
            list3.add(courses3);
            assertEquals(list3, courseImpl.findAllFiltered(0, 2, "Todos", ""));
            List<Courses> list4 = new ArrayList<>();
            list4.add(courses4);
            assertEquals(list4, courseImpl.findAllFiltered(5, 2, "Todos", ""));
            List<Courses> list5 = new ArrayList<>();
            list5.add(courses4);
            list5.add(courses3);
            list5.add(courses2);
            list5.add(courses6);
            list5.add(courses1);
            assertEquals(list5, courseImpl.findAllFiltered(0, 3, "Todos", ""));
            List<Courses> list7 = new ArrayList<>();
            list7.add(courses1);
            list7.add(courses2);
            list7.add(courses3);
            assertEquals(list7, courseImpl.findAllFiltered(0, 2, "FORENSE", ""));
            List<Courses> list8 = new ArrayList<>();
            list8.add(courses3);
            list8.add(courses2);
            list8.add(courses1);
            assertEquals(list8, courseImpl.findAllFiltered(0, 3, "FORENSE", ""));

            commentImpl.create(courses1.getCourseId(), users.getUsername(), "Descripcion de comentario", 5, "Comentario");
            commentImpl.create(courses2.getCourseId(), users.getUsername(), "Descripcion de comentario", 4, "Comentario");
            commentImpl.create(courses3.getCourseId(), users.getUsername(), "Descripcion de comentario", 3, "Comentario");
            commentImpl.create(courses4.getCourseId(), users.getUsername(), "Descripcion de comentario", 2, "Comentario");
            commentImpl.create(courses5.getCourseId(), users.getUsername(), "Descripcion de comentario", 1, "Comentario");

            List<Courses> list6 = new ArrayList<>();
            list6.add(courseImpl.findCoursesById(courses1.getCourseId()));
            list6.add(courseImpl.findCoursesById(courses2.getCourseId()));
            list6.add(courseImpl.findCoursesById(courses3.getCourseId()));
            list6.add(courseImpl.findCoursesById(courses4.getCourseId()));
            list6.add(courseImpl.findCoursesById(courses5.getCourseId()));
            assertEquals(list6, courseImpl.findAllFiltered(0, 4, "Todos", ""));

            List<Courses> list9 = new ArrayList<>();
            list9.add(courseImpl.findCoursesById(courses1.getCourseId()));
            list9.add(courseImpl.findCoursesById(courses2.getCourseId()));
            list9.add(courseImpl.findCoursesById(courses3.getCourseId()));
            assertEquals(list9, courseImpl.findAllFiltered(0, 4, "FORENSE", ""));


        } finally {
            if (courses1 != null) {
                courseImpl.remove(courses1.getCourseId());
            }
            if (courses2 != null) {
                courseImpl.remove(courses2.getCourseId());
            }
            if (courses3 != null) {
                courseImpl.remove(courses3.getCourseId());
            }
            if (courses4 != null) {
                courseImpl.remove(courses4.getCourseId());
            }
            if (courses5 != null) {
                courseImpl.remove(courses5.getCourseId());
            }
            if (courses6 != null) {
                courseImpl.remove(courses6.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }


        }
    }

    @Test
    public void testfindCoursesMoreInscriptions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        String username1 = "username1";
        String email1 = "username1@gmail.com";
        String username2 = "username2";
        String email2 = "username2@gmail.com";
        String username3 = "username3";
        String email3 = "username3@gmail.com";
        String password = "HolaAdios2000.";
        File image = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName = image.getName();
        FileInputStream fis = new FileInputStream(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        byte[] imageBytes = bos.toByteArray();

        fis.close();
        bos.close();

        Users users1 = null;
        Users users2 = null;
        Users users3 = null;
        String courseName1 = "Nombre de curso 1";
        String courseName2 = "Nombre de curso 2";
        String courseName3 = "Nombre de curso 3";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image2 = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName2 = image2.getName();
        Courses courses1 = null;
        Courses courses2 = null;
        Courses courses3 = null;

        try {
            users1 = userImpl.create(username1, email1, password, imageName, imageBytes);
            users2 = userImpl.create(username2, email2, password, imageName, imageBytes);
            users3 = userImpl.create(username3, email3, password, imageName, imageBytes);
            FileInputStream fis2 = new FileInputStream(image);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

            byte[] buffer2 = new byte[1024];
            int bytesRead2;

            while ((bytesRead2 = fis2.read(buffer2)) != -1) {
                bos2.write(buffer2, 0, bytesRead2);
            }

            byte[] imageBytes2 = bos2.toByteArray();

            fis2.close();
            bos2.close();

            courses1 = courseImpl.create(courseName1, price, category, imageName2, imageBytes2, description, users1.getUsername());
            courses2 = courseImpl.create(courseName2, price, category, imageName2, imageBytes2, description, users2.getUsername());
            courses3 = courseImpl.create(courseName3, price, category, imageName2, imageBytes2, description, users3.getUsername());

            inscriptionsImpl.signOn(username1, courses1.getCourseId());
            inscriptionsImpl.signOn(username2, courses1.getCourseId());
            inscriptionsImpl.signOn(username3, courses1.getCourseId());
            inscriptionsImpl.signOn(username1, courses2.getCourseId());
            inscriptionsImpl.signOn(username2, courses2.getCourseId());
            inscriptionsImpl.signOn(username1, courses3.getCourseId());

            List<Courses> courses = new ArrayList<>();
            courses.add(courses1);
            courses.add(courses2);
            courses.add(courses3);

            assertEquals(courses, courseImpl.findCoursesMoreInscriptions());

        } finally {
            if (courses1 != null) {
                courseImpl.remove(courses1.getCourseId());
            }
            if (courses2 != null) {
                courseImpl.remove(courses2.getCourseId());
            }
            if (courses3 != null) {
                courseImpl.remove(courses3.getCourseId());
            }
            if (users1 != null) {
                userImpl.Remove(users1.getUsername());
            }
            if (users2 != null) {
                userImpl.Remove(users2.getUsername());
            }
            if (users3 != null) {
                userImpl.Remove(users3.getUsername());
            }
        }
    }

}
