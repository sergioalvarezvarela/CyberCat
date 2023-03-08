package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Roles;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.exceptions.InstanceNotFoundException;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "userService")
public class UserImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);
    private static final int SALT_ROUNDS = 10;

    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;


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
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), getRoles(user.getTipo()));
    }


    @Transactional(readOnly = true)
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private static List<SimpleGrantedAuthority> getRoles(Roles tipo) {
        String rolesAsString = tipo.toString();
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        if (rolesAsString != null && !rolesAsString.isEmpty()) {
            String[] arrayOfRoles = rolesAsString.split(",");
            for (String role : arrayOfRoles) {
                roles.add(new SimpleGrantedAuthority(role));
            }
        }
        return roles;
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
        Users user = userRepository.create(new Users(username, email, BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS)), Roles.USER, LocalDateTime.now(), image));
        saveProfileImage(user.getUserId(), image, imageContents);
        return user;
    }

    @Transactional
    public void Remove(String username) {
        Users user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        }
        userRepository.remove(user);
    }

    @Transactional
    public void changePassword(String username,String oldPass, String password) throws AuthenticationException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Usuario {0} no existe", username));
        }
        if (passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS)));
            userRepository.update(user);

        } else {
            throw exceptionGenerationUtils.toAuthenticationException("auth.password.dont.matches", user.getUsername());
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


}
