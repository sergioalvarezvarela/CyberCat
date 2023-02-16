package es.cybercatapp.business.services;

import es.cybercatapp.business.entities.User;
import es.cybercatapp.business.exceptions.DuplicatedResourceException;
import es.cybercatapp.business.repositories.UserRepository;
import es.cybercatapp.business.utils.ExceptionGenerationUtils;
import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
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
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String SALT = "$2a$10$MN0gK0ldpCgN9jx6r0VYQO";
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
    public User create(String username, String email, String password, String address,
                       String image, byte[] imageContents) throws DuplicatedResourceException {
        if (userRepository.findByEmail(email) != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.EMAIL_FIELD, email,
                    Constants.DUPLICATED_INSTANCE_MESSAGE);
        }
        if (userRepository.findByUsername(username) != null) {
            throw exceptionGenerationUtils.toDuplicatedResourceException(Constants.USERNAME_FIELD, username,
                    Constants.DUPLICATED_INSTANCE_MESSAGE);
        }
        User user = userRepository.create(new User(username, email, BCrypt.hashpw(password, SALT), false, LocalDateTime.now(),image));
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

}
