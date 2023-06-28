import es.cybercatapp.Application;
import es.cybercatapp.model.entities.*;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.*;
import es.cybercatapp.model.repositories.InscriptionsRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class DiplomaTest {
    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private ModuleImpl moduleImpl;

    @Autowired
    private ContentImpl contentImpl;

    @Autowired
    private DiplomaImpl diplomaImpl;

    @Autowired
    private ContentUserImpl contentUserImpl;

    @Autowired
    private ModuleUserImpl moduleUserImpl;
    @Autowired
    private InscriptionsImpl inscriptionsImpl;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private static String toFechaFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        return date.format(formatter);
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
    public void testCreateDiploma() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;


        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            Diploma diploma = diplomaImpl.create(courses.getCourseId(), users.getUsername(), true);
            assertNull(diploma);
            Content content = contentImpl.createTeoricContent(module.getModuleId(), contentName, html);
            inscriptions = new Inscriptions(users, courses, false);
            contentUserImpl.updateUserContent(users.getUsername(), content.getContentId(), true);
            moduleUserImpl.updateModuleInscription(users.getUsername(), courses.getCourseId());
            inscriptionsImpl.updateInscriptionStatus(courses.getCourseId(), users.getUsername());
            diploma = diplomaImpl.create(courses.getCourseId(), users.getUsername(), true);
            assertEquals(diploma, diplomaImpl.findDiplomaById(diploma.getDiplomaId()));
            userImpl.modifyProfile(users.getUsername(), "newUsername","newemaul@gmail.com");
            assertThrows(DuplicatedResourceException.class, () -> {
                 diplomaImpl.create(courses.getCourseId(), "newUsername", true);
            });

        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove("newUsername");
            }

        }
    }

    @Test
    public void testGetDiploma() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;


        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            Content content = contentImpl.createTeoricContent(module.getModuleId(), contentName, html);
            inscriptions = new Inscriptions(users, courses, false);
            contentUserImpl.updateUserContent(users.getUsername(), content.getContentId(), true);
            moduleUserImpl.updateModuleInscription(users.getUsername(), courses.getCourseId());
            inscriptionsImpl.updateInscriptionStatus(courses.getCourseId(), users.getUsername());
            Diploma diploma = diplomaImpl.create(courses.getCourseId(), users.getUsername(), true);
            File pdf = new File("src/main/resources/static/pdf/diploma.pdf");
            PDDocument document = Loader.loadPDF(pdf);
            PDType0Font font = PDType0Font.load(document, new File("src/main/resources/static/fuentes/Roboto-Regular.ttf"));
            PDPage page = document.getPage(0);

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            float fontSize = 12.0f;
            String text = users.getEmail();
            float stringWidth = font.getStringWidth(text) / 1000 * fontSize;
            String text2 = courses.getCourse_name();
            float stringWidth2 = font.getStringWidth(text2) / 1000 * fontSize;
            String fecha = toFechaFormatter(LocalDate.now());
            float stringWidth3 = font.getStringWidth(fecha) / 1000 * fontSize;

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(420 - (stringWidth / 2), 320);
            contentStream.showText(text);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(420 - (stringWidth2 / 2), 240);
            contentStream.showText(text2);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(420 - (stringWidth3 / 2), 30);
            contentStream.showText(fecha);
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            byte[] diplomaBytes = outputStream.toByteArray();
            assertArrayEquals(diplomaImpl.getPdf(diploma.getDiplomaId()) ,diplomaBytes);

        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testFindDiplomaByCourseAndUsername() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        String moduleName = "Nombre de modulo";
        String contentName = "Contenido 1";
        String html = "Lore Ipsum";
        Inscriptions inscriptions = null;


        try {
            Modules module = moduleImpl.create(moduleName, courses.getCourseId());
            inscriptionsImpl.signOn(users.getUsername(), courses.getCourseId());
            Content content = contentImpl.createTeoricContent(module.getModuleId(), contentName, html);
            inscriptions = new Inscriptions(users, courses, false);
            contentUserImpl.updateUserContent(users.getUsername(), content.getContentId(), true);
            moduleUserImpl.updateModuleInscription(users.getUsername(), courses.getCourseId());
            inscriptionsImpl.updateInscriptionStatus(courses.getCourseId(), users.getUsername());
            Diploma diploma = diplomaImpl.create(courses.getCourseId(), users.getUsername(), true);
            assertEquals(diploma, diplomaImpl.findDiplomaByCourseAndUsername(courses.getCourseId(),users.getUsername()));



        } finally {
            if (inscriptions != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

}