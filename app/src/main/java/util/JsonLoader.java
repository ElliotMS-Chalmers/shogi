package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.IOException;

public class JsonLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T load(String path, TypeReference<T> typeReference) {
        try (InputStream inputStream = JsonLoader.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

