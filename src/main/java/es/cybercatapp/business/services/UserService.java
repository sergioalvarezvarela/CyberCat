package es.cybercatapp.business.services;

import es.cybercatapp.business.entities.User;
import es.cybercatapp.business.exceptions.DuplicatedResourceException;
import es.cybercatapp.business.utils.ExceptionGenerationUtils;
import es.cybercatapp.common.ConfigurationParameters;
import es.cybercatapp.common.Constants;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;

@Service
public class UserService {
    private static final String SALT = "$2a$10$MN0gK0ldpCgN9jx6r0VYQO";
    @Autowired
    ConfigurationParameters configurationParameters;

    @Autowired
    ExceptionGenerationUtils exceptionGenerationUtils;

    private File resourcesDir;

    @PostConstruct
    public void init() {
        resourcesDir = new File(configurationParameters.getResources());
    }
   /* @Transactional
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
    }*/
}
