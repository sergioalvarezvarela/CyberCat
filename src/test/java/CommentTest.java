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
public class CommentTest {
    @Autowired
    private CourseImpl courseImpl;

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private CommentImpl commentImpl;

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
    public void testCreateComment() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Comment comment = null;
        String description= "Description";
        int grade = 5;
        String commentary = "Comentario";

        try {
            comment = commentImpl.create(courses.getCourseId(),users.getUsername(),description,grade,commentary);
            assertEquals(comment,commentImpl.findCommentByUserAndCourse(courses.getCourseId(),users.getUsername()));
            assertThrows(DuplicatedResourceException.class, () -> {
                commentImpl.create(courses.getCourseId(),users.getUsername(),description,grade,commentary);
            });
        } finally {
            if (comment != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testUpdateComment() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Courses courses = getCourse(users);
        Comment comment = null;
        String description= "Description";
        int grade = 5;
        String commentary = "Comentario";

        try {
            comment = commentImpl.create(courses.getCourseId(),users.getUsername(),description,grade,commentary);
            comment.setGrade(2);
            comment.getCourses().setGrade(2);
            comment.getCourses().setPuntuation(2);
            commentImpl.update(comment.getCommentId(),description,2,commentary);
            assertEquals(comment,commentImpl.findCommentByUserAndCourse(courses.getCourseId(),users.getUsername()));
            Comment finalComment = comment;
            assertThrows(DuplicatedResourceException.class, () -> {
                commentImpl.update(finalComment.getCommentId(),description,2,commentary);
            });
        } finally {
            if (comment != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }

    @Test
    public void testRemoveComment() throws DuplicatedResourceException, IOException, UsernameNotFound, InstanceNotFoundException {
        Users users = getUser();
        Users user2 = userImpl.create("usuario","user@gmail.com","HolaAdios2000", null, null);
        Courses courses = getCourse(users);
        Comment comment = null;
        Comment comment2 = null;
        String description= "Description";
        int grade = 5;
        String commentary = "Comentario";

        try {
            comment = commentImpl.create(courses.getCourseId(),users.getUsername(),description,grade,commentary);
            comment2 = commentImpl.create(courses.getCourseId(),user2.getUsername(),description,grade,commentary);
            commentImpl.remove(comment.getCommentId());
            comment2.getCourses().setTotal_comments(1);
            comment2.getCourses().setGrade(5);
            comment2.getCourses().setPuntuation(5);
            List<Comment> list= new ArrayList<>(Collections.emptyList());
            list.add(comment2);
            assertEquals(list,commentImpl.findCommentsbyCourse(courses.getCourseId(),0));
            commentImpl.remove(comment2.getCommentId());
            list.remove(0);
            assertEquals(list,commentImpl.findCommentsbyCourse(courses.getCourseId(),0));


        } finally {
            if (comment != null) {
                courseImpl.remove(courses.getCourseId());
                userImpl.Remove(users.getUsername());
            }

        }
    }
}
