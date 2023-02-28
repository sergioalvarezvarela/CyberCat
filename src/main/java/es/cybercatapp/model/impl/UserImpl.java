package es.cybercatapp.model.impl;

import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import es.cybercatapp.model.entities.Users;
import es.cybercatapp.model.exceptions.AuthenticationException;
import es.cybercatapp.model.exceptions.DuplicatedResourceException;
import es.cybercatapp.model.repositories.UserRepository;
import es.cybercatapp.model.utils.ExceptionGenerationUtils;
import org.apache.commons.io.IOUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

@Service
public class UserImpl {

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
        Users user = userRepository.create(new Users(username, email, BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS)), false, LocalDateTime.now(), image));
        saveProfileImage(user.getUserId(), image, imageContents);
        return user;
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


    @Transactional(readOnly = true)
    public Users login(String email, String clearPassword) throws AuthenticationException {
        Users user = userRepository.findByEmail(email);

        if (user == null) {
            throw exceptionGenerationUtils.toAuthenticationException("auth.invalid.email", email);
        } else {


            boolean matches = BCrypt.checkpw(clearPassword, user.getPassword());
            if (!matches) {
                throw exceptionGenerationUtils.toAuthenticationException("auth.invalid.password", email);
            }
            return user;
        }
    }
}