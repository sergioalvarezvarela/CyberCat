package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Inscriptions;
import es.cybercatapp.model.entities.Roles;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.CourseRepository;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service(value = "userImpl")
public class UserImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);
    private static final int SALT_ROUNDS = 10;

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    private File resourcesDir;

    @PostConstruct
    public void init() {
        resourcesDir = new File(configurationParameters.getResources());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Users> optUser = Optional.ofNullable(userRepository.findByUsername(userName));
        if (!optUser.isPresent()) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", userName));
        }
        Users user = optUser.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getTipo().name()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), grantedAuthorities);
    }


    @Transactional(readOnly = true)
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }



    @Transactional
    public Users create(String username, String email, String password,
                        String image, byte[] imageContents) throws DuplicatedResourceException {
        if (userRepository.findByEmail(email) != null) {

            throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.EMAIL_FIELD, email,
                    "registration.duplicated.exception");
        }
        if (userRepository.findByUsername(username) != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.USERNAME_FIELD, username,
                    "registration.duplicated.exception");
        }
        Users user = userRepository.create(new Users(username, email, BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS)), Roles.ROLE_USER, LocalDateTime.now(), image));
        saveProfileImage(user.getUserId(), image, imageContents);
        return user;
    }

    @Transactional
    public void Remove(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        } else {
            List<Courses> coursesbyUserOwner = user.getCourses();
            for (Courses courses : coursesbyUserOwner) {
                courseRepository.remove(courses);
            }
            userRepository.remove(user);
        }
    }


    @Transactional
    public void changePassword(String username, String oldPass, String password) throws AuthenticationException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        }
        if (passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS)));
            userRepository.update(user);
            modifyAuthentification(user.getUsername(), user.getPassword());
        } else {
            throw exceptionGenerationUtils.toAuthenticationException("auth.password.dont.matches", user.getUsername());
        }

    }


    @Transactional
    public void modifyProfile(String username, String newusername, String newemail) throws DuplicatedResourceException {
        Users user = userRepository.findByUsername(username);
        Users user1 = userRepository.findByUsername(newusername);
        Users user2 = userRepository.findByEmail(newemail);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        } else {
            if (user1 != null && user2 != null) {
                throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.USERNAME_FIELD, newusername,
                        "user.duplicated.exception");
            } else {
                user.setUsername(newusername);
                user.setEmail(newemail);
                userRepository.update(user);
                modifyAuthentification(newusername, user.getPassword());
            }

        }

    }

    @Transactional
    public void updateProfileImage(String username, String image, byte[] imageContents) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        } else {
            if (image != null && image.trim().length() > 0 && imageContents != null) {
                try {
                    deleteProfileImage(user.getUserId(), image);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
                saveProfileImage(user.getUserId(), image, imageContents);
                user.setImagen_perfil(image);
            }

        }


    }


    @Transactional
    public byte[] getImage(Long id) throws InstanceNotFoundException {
        Users user = userRepository.findById(id);
        try {
            return getProfileImage(id, user.getImagen_perfil());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    private byte[] getProfileImage(Long id, String image) throws IOException {
        if (image != null && image.trim().length() > 0) {
            File userDir = new File(resourcesDir, id.toString());
            File profilePicture = new File(userDir, image);
            try (FileInputStream input = new FileInputStream(profilePicture)) {
                return IOUtils.toByteArray(input);
            }
        }
        return null;
    }

    private void deleteProfileImage(Long id, String image) throws IOException {
        if (image != null && image.trim().length() > 0) {
            File userDir = new File(resourcesDir, id.toString());
            File profilePicture = new File(userDir, image);
            Files.delete(profilePicture.toPath());
        }
    }

    private void saveProfileImage(Long id, String image, byte[] imageContents) {
        if (image != null && image.trim().length() > 0 && imageContents != null) {
            File userDir = new File(resourcesDir, id.toString());
            userDir.mkdirs();
            File profilePicture = new File(userDir, image);
            try (FileOutputStream outputStream = new FileOutputStream(profilePicture);) {
                IOUtils.copy(new ByteArrayInputStream(imageContents), outputStream);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void modifyAuthentification(String username, String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        User updatedUser = new User(
                username,
                password,
                currentUser.isEnabled(),
                currentUser.isAccountNonExpired(),
                currentUser.isCredentialsNonExpired(),
                currentUser.isAccountNonLocked(),
                currentUser.getAuthorities()
        );
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }


}
