package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Diploma;
import es.cybercatapp.model.entities.Inscriptions;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.DiplomaRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

@Service(value = "diplomaImpl")
public class DiplomaImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiplomaRepository diplomaRepository;

    @Autowired
    private InscriptionsImpl inscriptionsImpl;

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    private File resourcesDir;

    @PostConstruct
    public void init() {
        resourcesDir = new File(configurationParameters.getResources());
    }

    @Transactional
    public Diploma create(Long courseId, String username, boolean payment) throws InstanceNotFoundException, DuplicatedResourceException, IOException {
        Diploma diploma1 = diplomaRepository.findContentsByContentNameAndModule(courseId, username);
        Diploma diploma;

        Inscriptions inscriptions = inscriptionsImpl.findInscription(courseId, username);
        if (!inscriptions.isCompleted()) {
            return null;
        }
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        }
        Courses courses = courseRepository.findById(courseId);
        File pdf = new File("src/main/resources/static/pdf/diploma.pdf");
        try (PDDocument document = Loader.loadPDF(pdf)) {
            PDType0Font font = PDType0Font.load(document, new File("src/main/resources/static/fuentes/Roboto-Regular.ttf"));
            PDPage page = document.getPage(0);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float fontSize = 12.0f;
                String text = user.getEmail();
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

            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            byte[] diplomaBytes = outputStream.toByteArray();
            diploma = new Diploma(courseId+"_"+username+".pdf",LocalDate.now(), payment, courses, user);
            if (diploma1!=null){
                deleteDiplomaPdfById(diploma1.getDiplomaId());
                savePdf(diploma1.getDiplomaId(),diploma.getPdf(),diplomaBytes);
                throw exceptionGenerationUtils.toDuplicatedResourceException("Diploma", diploma.getPdf(),
                        "diploma.duplicated.exception");
            } else{
                diplomaRepository.create(diploma);
                savePdf(diploma.getDiplomaId(),diploma.getPdf(),diplomaBytes);
            }
            return diploma;

        }
    }

    @Transactional(readOnly = true)
    public Diploma findDiplomaById(long diplomaId) throws InstanceNotFoundException {
        return diplomaRepository.findById(diplomaId);
    }

    @Transactional(readOnly = true)
    public Diploma findDiplomaByCourseAndUsername(long courseId, String username) {
        return diplomaRepository.findContentsByContentNameAndModule(courseId,username);
    }


    private static String toFechaFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        return date.format(formatter);
    }

    private void savePdf(Long id, String image, byte[] imageContents) {
        if (image != null && image.trim().length() > 0 && imageContents != null) {
            File pdfDir = new File(resourcesDir, id.toString());
            pdfDir.mkdirs();
            File profilePicture = new File(pdfDir, image);
            try (FileOutputStream outputStream = new FileOutputStream(profilePicture);) {
                IOUtils.copy(new ByteArrayInputStream(imageContents), outputStream);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Transactional
    public byte[] getPdf(Long id) throws InstanceNotFoundException {
        Diploma diploma = diplomaRepository.findById(id);
        try {
            return getPdfDiploma(id, diploma.getPdf());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    private byte[] getPdfDiploma(Long id, String image) throws IOException {
        if (image != null && image.trim().length() > 0) {
            File pdfDir = new File(resourcesDir, id.toString());
            File pdfCont = new File(pdfDir, image);
            try (FileInputStream input = new FileInputStream(pdfCont)) {
                return IOUtils.toByteArray(input);
            }
        }
        return null;
    }

    @Transactional
    public void deletePdfByPrefix(String prefix) throws IOException {
        File[] pdfDirs = resourcesDir.listFiles(File::isDirectory);

        if (pdfDirs != null) {
            for (File pdfDir : pdfDirs) {
                File[] pdfFiles = pdfDir.listFiles((dir, name) -> name.toLowerCase().startsWith(prefix) && name.toLowerCase().endsWith(".pdf"));

                if (pdfFiles != null) {
                    for (File pdfFile : pdfFiles) {
                        Files.delete(pdfFile.toPath());
                    }
                }
            }
        }
    }
    @Transactional
    public void deletePdfContaining(String searchString) throws IOException {
        File[] pdfDirs = resourcesDir.listFiles(File::isDirectory);

        if (pdfDirs != null) {
            for (File pdfDir : pdfDirs) {
                File[] pdfFiles = pdfDir.listFiles((dir, name) -> name.toLowerCase().contains(searchString) && name.toLowerCase().endsWith(".pdf"));

                if (pdfFiles != null) {
                    for (File pdfFile : pdfFiles) {
                        Files.delete(pdfFile.toPath());
                    }
                }
            }
        }
    }

    private void deleteDiplomaPdfById(Long id) throws IOException {
        File pdfDir = new File(resourcesDir, id.toString());

        if (pdfDir.exists() && pdfDir.isDirectory()) {
            File[] pdfFiles = pdfDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (pdfFiles != null) {
                for (File pdfFile : pdfFiles) {
                    Files.delete(pdfFile.toPath());
                }
            }
        }
    }









}
