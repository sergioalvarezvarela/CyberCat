package es.cybercatapp.model.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class ProfilePhoto {
    private static Map<String, byte[]> photos;
    private static Random random;



    private static byte[] getPhotoBytesFromPath(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    static {
        photos = new HashMap<>();
        random = new Random();
        try {
            photos.put("profile1.jpeg", getPhotoBytesFromPath("src/main/resources/static/images/profile1.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            photos.put("profile2.jpeg", getPhotoBytesFromPath("src/main/resources/static/images/profile2.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            photos.put("profile3.jpeg",getPhotoBytesFromPath("src/main/resources/static/images/profile3.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            photos.put("profile4.jpeg", getPhotoBytesFromPath("src/main/resources/static/images/profile4.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            photos.put("profile5.jpeg", getPhotoBytesFromPath("src/main/resources/static/images/profile5.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            photos.put("profile6.jpeg",getPhotoBytesFromPath("src/main/resources/static/images/profile6.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Map<String, byte[]> getPhotos() {
        return photos;
    }

    public static void setPhotos(Map<String, byte[]> photos) {
        ProfilePhoto.photos = photos;
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        ProfilePhoto.random = random;
    }

    public PhotoData getRandomPhoto() {
        if (photos.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(photos.size());
        String randomKey = (String) photos.keySet().toArray()[randomIndex];
        byte[] randomBytes = photos.get(randomKey);
        return new PhotoData(randomKey, randomBytes);
    }

    public static class PhotoData {
        private String name;
        private byte[] bytes;

        public PhotoData(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public String getName() {
            return name;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }
}