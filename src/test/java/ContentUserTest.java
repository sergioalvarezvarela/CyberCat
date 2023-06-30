import es.cybercatapp.Application;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ContentUserTest {
    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private ContentImpl contentImpl;

    @Autowired
    private InscriptionsImpl inscriptionsImpl;
    @Autowired
    private ContentUserImpl contentUserImpl;

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
    public void testUpdateUserContentInscription() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;

        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            Content content = contentImpl.createTeoricContent(module.getModuleId(),contentName,html);
            List<ContentUser> list1 = new ArrayList<>(Collections.emptyList());
            assertEquals(list1,contentUserImpl.findListContentUser(users.getUsername(),module.getModuleId()));
            contentUserImpl.updateContentInscription(users.getUsername(),module.getModuleId());
            ContentUser contentUser = new ContentUser(users,content, null);
            list1.add(contentUser);
            assertEquals(list1,contentUserImpl.findListContentUser(users.getUsername(),module.getModuleId()));
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testUpdateUserContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;


        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            Content content = contentImpl.createTeoricContent(module.getModuleId(),contentName,html);
            inscriptions = new Inscriptions(users,courses,false);
            List<ContentUser> list1 = new ArrayList<>(Collections.emptyList());
            contentUserImpl.updateUserContent(users.getUsername(),content.getContentId(),true);
            ContentUser contentUser = new ContentUser(users,content, true);
            list1.add(contentUser);
            assertEquals(list1,contentUserImpl.findListContentUser(users.getUsername(),module.getModuleId()));
        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

}