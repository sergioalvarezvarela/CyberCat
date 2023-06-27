import es.cybercatapp.Application;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.ContentImpl;
import es.cybercatapp.model.impl.CourseImpl;
import es.cybercatapp.model.impl.ModuleImpl;
import es.cybercatapp.model.impl.UserImpl;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.ModuleRepository;
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
public class ModuleTest {
    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ContentImpl contentImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Modules inicializarModulos(Modules modules) {
        return moduleRepository.inicializarContenidos(modules);
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
    public void testCreateModule() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        try {


            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            assertEquals(module, moduleImpl.findModulesById(module.getModuleId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                moduleImpl.create(moduleName, courses.getCourseId());
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
    public void testRemoveModule() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            moduleImpl.remove(module.getModuleId());
            assertThrows(InstanceNotFoundException.class, () -> {
                moduleImpl.findModulesById(module.getModuleId());
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
    public void testUpdateModule() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Modules module1 = null;
        String moduleName1 = "Nombre de modulo";
        try {
            module1 = moduleImpl.create(moduleName1, courses.getCourseId());
            Modules finalModule = module1;
            assertThrows(DuplicatedResourceException.class, () -> {
                moduleImpl.update(courses.getCourseId(), finalModule.getModuleId(), "Nombre de modulo");
            });
            moduleImpl.update(courses.getCourseId(), module1.getModuleId(), "Nuevo Nombre");
            module1.setModuleName("Nuevo Nombre");
            assertEquals(module1, moduleImpl.findModulesById(module1.getModuleId()));

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
    public void testUpdateModulePositions() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName1 = "Nombre de modulo";
        try {
            Modules module1 = moduleImpl.create(moduleName1, courses.getCourseId());
            contentImpl.createTeoricContent(module1.getModuleId(), "Contenido 1", "Lore Ipsum");
            contentImpl.createTeoricContent(module1.getModuleId(), "Contenido 2", "Lore Ipsum");
            Modules modules = moduleImpl.findModulesById(module1.getModuleId());
            modules = inicializarModulos(modules);
            modules.getContents().get(0).setContentPosition(1);
            modules.getContents().get(1).setContentPosition(0);
            moduleImpl.updatePositions(modules);
            assertEquals(modules, moduleImpl.findModulesById(module1.getModuleId()));
        } finally {
            if (courses != null) {
                courseImpl.remove(courses.getCourseId());
            }
            if (users != null) {
                userImpl.Remove(users.getUsername());
            }

        }
    }
}
