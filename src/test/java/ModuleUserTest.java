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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ModuleUserTest {
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
    public void testUpdateModuleInscription() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;
        String moduleName2 = "Nombre de modulo 2";

        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            ModuleUser moduleUser = new ModuleUser(users, module, null);
            assertEquals(moduleUserImpl.findListModuleUser(users.getUsername(),courses.getCourseId()).get(0),moduleUser);
            Content content = contentImpl.createTeoricContent(module.getModuleId(),contentName,html);
            contentUserImpl.updateUserContent(users.getUsername(),content.getContentId(),false);
            moduleUserImpl.updateModuleInscription(users.getUsername(),courses.getCourseId());
            moduleUser = new ModuleUser(users, module, false);
            assertEquals(moduleUserImpl.findListModuleUser(users.getUsername(),courses.getCourseId()).get(0),moduleUser);
            contentUserImpl.updateUserContent(users.getUsername(),content.getContentId(),true);
            moduleUserImpl.updateModuleInscription(users.getUsername(),courses.getCourseId());
            moduleUser = new ModuleUser(users, module, true);
            assertEquals(moduleUserImpl.findListModuleUser(users.getUsername(),courses.getCourseId()).get(0),moduleUser);
            Modules module2 = moduleImpl.create(moduleName2, courses.getCourseId());
            moduleUserImpl.updateModuleInscription(users.getUsername(),courses.getCourseId());
            moduleUser = new ModuleUser(users, module2, null);
            assertEquals(moduleUserImpl.findListModuleUser(users.getUsername(),courses.getCourseId()).get(1),moduleUser);


        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }


    }

    @Test
    public void testRemoveModuleInscription() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        Inscriptions inscriptions = null;


        try {
            moduleImpl.create(moduleName, courses.getCourseId());
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            inscriptions = new Inscriptions(users,courses,false);
            List<ModuleUser> list1 = new ArrayList<>(Collections.emptyList());
            moduleUserImpl.remove(courses.getCourseId(), users.getUsername());
            assertEquals(list1,moduleUserImpl.findListModuleUser(users.getUsername(),courses.getCourseId()));



        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }


    }
}
