import es.cybercatapp.Application;
import es.cybercatapp.model.entities.DummyAuthentication;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.exceptions.UsernameNotFound;
import es.cybercatapp.model.impl.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserTest {

    @Autowired
    private UserImpl userImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserAndFindUser() throws DuplicatedResourceException, IOException, UsernameNotFound {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName = image.getName();
        Users user2 = null;
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

            user2 = userImpl.create(username, email, password, imageName, imageBytes);
            assertEquals(user2, userImpl.findByUsername(username));
            assertThrows(DuplicatedResourceException.class, () -> {
                userImpl.create(username, "email@gmail.com", password, imageName, imageBytes);
            });
            assertThrows(DuplicatedResourceException.class, () -> {
                userImpl.create("username", email, password, imageName, imageBytes);
            });
        } finally {
            if (user2 != null) {
                userImpl.Remove(username);
            }

        }

    }

    @Test
    public void testRemove() throws IOException, DuplicatedResourceException, UsernameNotFound {
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

        userImpl.create(username, email, password, imageName, imageBytes);
        userImpl.Remove(username);
        assertThrows(UsernameNotFound.class, () -> {
            userImpl.findByUsername(username);
        });
        assertThrows(UsernameNotFound.class, () -> {
            userImpl.Remove("usernameNotRegistered");
        });


    }

    @Test
    public void testModify() throws IOException, DuplicatedResourceException, UsernameNotFound {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image1 = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName1 = image1.getName();
        Users user= null;
        try {
            FileInputStream fis1 = new FileInputStream(image1);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis1.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes1 = bos.toByteArray();

            fis1.close();
            bos.close();

            user = userImpl.create(username, email, password, imageName1, imageBytes1);
            assertThrows(DuplicatedResourceException.class, () -> {
                userImpl.modifyProfile(username,username,email);
            });
            List <GrantedAuthority> roles = new ArrayList<>();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
            roles.add(grantedAuthority);
            User dummyUser = new User(username,password,roles);
            Authentication dummyAuthentification = new DummyAuthentication(dummyUser);
            SecurityContextHolder.getContext().setAuthentication(dummyAuthentification);
            userImpl.modifyProfile(user.getUsername(),"newusername",user.getEmail());
            assertEquals("newusername",userImpl.findByUsername("newusername").getUsername());
            userImpl.modifyProfile("newusername","newusername","newusername@gmail.com");
            assertEquals("newusername@gmail.com",userImpl.findByUsername("newusername").getEmail());
            assertThrows(UsernameNotFound.class, () -> {
                userImpl.modifyProfile(username,username,email);
            });

        } finally {
            if (user != null) {
                userImpl.Remove("newusername");
            }

        }

    }


    @Test
    public void testUpdateImageAndFindImage() throws IOException, DuplicatedResourceException, UsernameNotFound, InstanceNotFoundException {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image1 = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName1 = image1.getName();
        File image2 = new File("src/test/resources/images/profilephoto2.jpeg");
        String imageName2 = image2.getName();
        Users user= null;
        try {
            FileInputStream fis1 = new FileInputStream(image1);

            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();


            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis1.read(buffer)) != -1) {
                bos1.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes1 = bos1.toByteArray();


            fis1.close();
            bos1.close();

            FileInputStream fis2 = new FileInputStream(image2);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            byte[] buffer2 = new byte[1024];
            int bytesRead2;
            while ((bytesRead2 = fis2.read(buffer2)) != -1) {
                bos2.write(buffer2, 0, bytesRead2);
            }

            byte[] imageBytes2 = bos2.toByteArray();
            fis2.close();
            bos2.close();
            user = userImpl.create(username, email, password, imageName1, imageBytes1);
            userImpl.updateProfileImage(username,imageName2,imageBytes2);
            assertEquals(imageName2,userImpl.findByUsername(username).getImagen_perfil());
            assertArrayEquals(userImpl.getImage(user.getUserId()),imageBytes2);
            assertThrows(UsernameNotFound.class, () -> {
                userImpl.updateProfileImage("userNotExisting",imageName1,imageBytes1);
            });
        } finally {
            if (user != null) {
                userImpl.Remove(username);
            }

        }

    }

    @Test
    public void testChangePassword() throws IOException, DuplicatedResourceException, UsernameNotFound, AuthenticationException {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image1 = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName1 = image1.getName();
        Users user= null;
        try {
            FileInputStream fis1 = new FileInputStream(image1);

            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();


            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis1.read(buffer)) != -1) {
                bos1.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes1 = bos1.toByteArray();


            fis1.close();
            bos1.close();

            user = userImpl.create(username, email, password, imageName1, imageBytes1);
            assertThrows(AuthenticationException.class, () -> {
                userImpl.changePassword(username,"HolaAdios3000.","HolaAdios3000.");
            });
            assertThrows(UsernameNotFound.class, () -> {
                userImpl.changePassword("usernameNotFound",password,"HolaAdios3000.");
            });
            List <GrantedAuthority> roles = new ArrayList<>();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
            roles.add(grantedAuthority);
            User dummyUser = new User(username,password,roles);
            Authentication dummyAuthentification = new DummyAuthentication(dummyUser);
            SecurityContextHolder.getContext().setAuthentication(dummyAuthentification);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userImpl.changePassword(username,password,"HolaAdios3000.");
            assertTrue(passwordEncoder.matches("HolaAdios3000.", userImpl.findByUsername(username).getPassword()));
        } finally {
            if (user != null) {
                userImpl.Remove(username);
            }

        }

    }

    @Test
    void testLoadUserByUsername() throws DuplicatedResourceException, IOException, UsernameNotFound {
        String username = "sergio.alvarez.varela";
        String email = "sergio.alvarez.varela@udc.es";
        String password = "HolaAdios2000.";
        File image1 = new File("src/test/resources/images/profilephoto.jpeg");
        String imageName1 = image1.getName();
        Users user= null;
        try {
            FileInputStream fis1 = new FileInputStream(image1);

            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();


            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis1.read(buffer)) != -1) {
                bos1.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes1 = bos1.toByteArray();


            fis1.close();
            bos1.close();

            user = userImpl.create(username, email, password, imageName1, imageBytes1);
            UserDetails userDetails = userImpl.loadUserByUsername(username);
            assertEquals(username,userDetails.getUsername());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            assertTrue(passwordEncoder.matches(password, userDetails.getPassword()));
            assertThrows(UsernameNotFoundException.class, () -> {
                userImpl.loadUserByUsername("userNotExists");
            });
        } finally {
            if (user != null) {
                userImpl.Remove(username);
            }

        }


    }
}
