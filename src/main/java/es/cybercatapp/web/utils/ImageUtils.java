package es.cybercatapp.web.utils;

import java.util.Base64;

public class ImageUtils {

    public static String toString64 (byte[] image){
        String encodedImage = null;
        if (image != null) {
            encodedImage = Base64.getEncoder().encodeToString(image);
        }
        return encodedImage;
    }

}
