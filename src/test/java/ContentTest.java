import es.cybercatapp.Application;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.services.ContentImpl;
import es.cybercatapp.model.services.CourseImpl;
import es.cybercatapp.model.services.ModuleImpl;
import es.cybercatapp.model.services.UserImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ContentTest {
    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private ContentImpl contentImpl;

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

    private Courses getCourse(Users users) throws IOException, UsernameNotFound {
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

    public Modules getModule() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Modulo 1";
        return moduleImpl.create(moduleName, courses.getCourseId());
    }

    @Test
    public void testCreateTeoricContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";

        try {

            StringContent content = contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void testCreateTestContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";

        try {

            TestQuestions content = contentImpl.createTestQuestionContent(modules.getModuleId(),contentName,enunciado);
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.createTestQuestionContent(modules.getModuleId(),contentName,enunciado);
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void testPuzzleContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";
        String frase = "Frase";
        String fraseCorrecta = "Frase Correcta";
        String words = "Palabras";


        try {

            StringComplete content = contentImpl.createStringCompleteContent(modules.getModuleId(),contentName,enunciado, frase, fraseCorrecta, words);
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.createStringCompleteContent(modules.getModuleId(),contentName,enunciado, frase, fraseCorrecta, words);
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void removeContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";

        try {

            StringContent content = contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            contentImpl.remove(content.getContentId());
            assertThrows(InstanceNotFoundException.class, () -> {
                contentImpl.findByContentId(content.getContentId());
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void findByPosition() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";

        try {

            StringContent content = contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            assertEquals(content,contentImpl.findContentByModuleIdAndPosition(modules.getModuleId(),1));
            assertNull(contentImpl.findContentByModuleIdAndPosition(modules.getModuleId(),10));
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void updateContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";

        try {

            StringContent content = contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            content.setContentName("New Name");
            contentImpl.contentUpdate(modules.getModuleId(),content.getContentId(),"New Name");
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.contentUpdate(modules.getModuleId(),content.getContentId(),"New Name");
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void updateTeoricContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        String html2 = "New Lore Ipsum";

        try {

            StringContent content = contentImpl.createTeoricContent(modules.getModuleId(),contentName,html);
            content.setHtml(html2);
            contentImpl.stringContentUpdate(html2,content.getContentId());
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.stringContentUpdate(html2,content.getContentId());
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void updateTestContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";
        String enunciado2 = "Enunciado nuevo";
        String opcion1 = "Opcion 1 nuevo";
        String opcion2 = "Opcion 2 nuevo";
        String opcion3 = "Opcion 3 nuevo";
        String opcion4 = "Opcion 4 nuevo";
        try {

            TestQuestions content = contentImpl.createTestQuestionContent(modules.getModuleId(),contentName,enunciado);
            content.setOption1(opcion1);
            content.setOption2(opcion2);
            content.setOption3(opcion3);
            content.setOption4(opcion4);
            content.setCorrect(2);
            contentImpl.testContentUpdate(content.getContentId(),enunciado2, opcion1, opcion2, opcion3, opcion4, 2);
            assertEquals(content, contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.testContentUpdate(content.getContentId(),enunciado2, opcion1, opcion2, opcion3, opcion4, 2);
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void updatePuzzleContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";
        String frase = "Frase";
        String fraseCorrecta = "Frase Correcta";
        String words = "Palabras";
        String enunciado2 = "Enunciado nuevo";
        String frase2 = "Frase nueva";
        String fraseCorrecta2 = "Frase Correcta nueva";
        String words2 = "Palabras" +
                "nueva";

        try {

            StringComplete content = contentImpl.createStringCompleteContent(modules.getModuleId(),contentName,enunciado, frase, fraseCorrecta, words);
            content.setEnunciado(enunciado2);
            content.setSentence(frase2);
            content.setCorrectSentence(fraseCorrecta2);
            content.setContent(words2);
            contentImpl.puzzleContentUpdate(content.getContentId(),enunciado2,frase2,fraseCorrecta2,words2);
            assertEquals(content,contentImpl.findByContentId(content.getContentId()));
            assertThrows(DuplicatedResourceException.class, () -> {
                contentImpl.puzzleContentUpdate(content.getContentId(),enunciado2,frase2,fraseCorrecta2,words2);
            });
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

    @Test
    public void checkTestContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";
        try {

            TestQuestions content = contentImpl.createTestQuestionContent(modules.getModuleId(),contentName,enunciado);
            assertTrue(contentImpl.isTestCorrect(content.getContentId(),1));
            assertFalse(contentImpl.isTestCorrect(content.getContentId(),2));
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }


    @Test
    public void checkPuzzleContent() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Modules modules = getModule();
        String contentName = "Contenido 1";
        String enunciado = "Enunciado";
        String frase = "Enunciado de __ para test __";
        String frasecorrecta = "Enunciado de ejemplo para test correcto";
        String words = "ejemplo\nPalabra1\ncorrecto";


        try {

            StringComplete content = contentImpl.createStringCompleteContent(modules.getModuleId(), contentName, enunciado, frase, frasecorrecta, words);
            PuzzleCheckReturn puzzleCheckReturn1 = contentImpl.isPuzzleCorrect(content.getContentId(), "ejemplo\r\ncorrecto");
            PuzzleCheckReturn puzzleCheckReturn2 = contentImpl.isPuzzleCorrect(content.getContentId(), "ejemplo\r\nincorrecto");
            PuzzleCheckReturn puzzleCheckReturn3 = contentImpl.isPuzzleCorrect(content.getContentId(),"\r\n");
            assertTrue(puzzleCheckReturn1.isCorrect());
            assertFalse(puzzleCheckReturn2.isCorrect());
            assertFalse(puzzleCheckReturn3.isCorrect());
        } finally {
            if (modules != null) {
                courseImpl.remove(modules.getCourseId().getCourseId());
                userImpl.Remove(modules.getCourseId().getUser_owner().getUsername());
            }

        }
    }

}
