package es.cybercatapp.service.utils;

import es.cybercatapp.model.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtils {

    public static String toString64 (byte[] image){
        String encodedImage = null;
        if (image != null) {
            encodedImage = Base64.getEncoder().encodeToString(image);
        }
        return encodedImage;
    }

    public String getImageType(String image) {
        if (image != null && image.trim().length() > 0) {
            Path path = Paths.get(image);
            String fileType = "";
            try {
                fileType = Files.probeContentType(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileType;
        }
        return null;
    }

}