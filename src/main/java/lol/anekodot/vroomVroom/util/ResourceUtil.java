package lol.anekodot.vroomVroom.util;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    public static String getResourceAsString(String fileName) {
        try (InputStream input = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IOException("Resource not found: " + fileName);
            }

            return new String(input.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getResourceAsBytes(String fileName) {
        try (InputStream input = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IOException("Resource not found: " + fileName);
            }

            return input.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getResource(String fileName) {
        try (InputStream input = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IOException("Resource not found: " + fileName);
            }

            return input;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
