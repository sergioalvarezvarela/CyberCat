import es.cybercatapp.Application;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.services.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class InscriptionTest {
    @Autowired
    private InscriptionsImpl inscriptionsImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private ContentImpl contentImpl;

    @Autowired
    private ContentUserImpl contentUserImpl;
    @Autowired
    private ModuleUserImpl moduleUserImpl;


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

    private Courses getCourse(Users users) throws IOException, DuplicatedResourceException, UsernameNotFound {
        String courseName = "Nombre de curso";
        float price = (float) 10.55;
        String category = String.valueOf(Category.valueOf("FORENSE"));
        String description = "Description";
        File image = new File("src/test/resources/images/coursephoto.jpeg");
        String imageName = image.getName();
        Courses courses = null;

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

        return courseImpl.create(courseName, price, category, imageName, imageBytes, description, users.getUsername());
    }

    @Test
    public void testCreateInscriptions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Inscriptions inscriptions = null;

        try {
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            assertEquals(inscriptions,inscriptionsImpl.findInscription(courses.getCourseId(),users.getUsername()));
            assertThrows(DuplicatedResourceException.class, () -> {
                inscriptionsImpl.signOn(users.getUsername(),courses.getCourseId());
            });
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testRemoveInscriptions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Inscriptions inscriptions = null;
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";

        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            contentImpl.createTeoricContent(module.getModuleId(),contentName,html);
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            inscriptionsImpl.remove(inscriptions.getUsers().getUsername(),inscriptions.getCourses().getCourseId());
            Inscriptions finalInscriptions = inscriptions;
            assertThrows(InstanceNotFoundException.class, () -> {
                inscriptionsImpl.findInscription(finalInscriptions.getId().getCourseId(), finalInscriptions.getUsers().getUsername());
            });
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testUpdateInscriptions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;

        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            Content content = contentImpl.createTeoricContent(module.getModuleId(),contentName,html);
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            inscriptionsImpl.updateInscriptionStatus(courses.getCourseId(),users.getUsername());
            assertEquals(inscriptions,inscriptionsImpl.findInscription(courses.getCourseId(),users.getUsername()));
            contentUserImpl.updateUserContent(users.getUsername(),content.getContentId(), true);
            moduleUserImpl.updateModuleInscription(users.getUsername(),courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,true);
            inscriptionsImpl.updateInscriptionStatus(courses.getCourseId(),users.getUsername());
            assertEquals(inscriptions,inscriptionsImpl.findInscription(courses.getCourseId(),users.getUsername()));
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testFindInscriptionsByUsers() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Inscriptions inscriptions = null;

        try {
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            assertEquals(inscriptions.getCourses(),inscriptionsImpl.findCoursesInscriptionsByUser(users.getUsername()).get(0));
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }
}
