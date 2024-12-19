package model.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Utility class for loading JSON files and resources.
 * This class provides methods for reading JSON data from either resource files or filesystem paths
 * and deserializing it into Java objects using Jackson's {@link ObjectMapper}.
 */
public class JsonLoader {

    /**
     * The shared {@link ObjectMapper} instance used for JSON processing.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Loads a JSON resource from the classpath and deserializes it into the specified type.
     *
     * @param path           The path to the JSON resource within the classpath.
     * @param typeReference  A {@link TypeReference} representing the type to deserialize into.
     * @param <T>            The type of the object to return.
     * @return The deserialized object, or {@code null} if an error occurs.
     * @throws IllegalArgumentException If the resource cannot be found.
     */
    public static <T> T loadResource(String path, TypeReference<T> typeReference) {
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

    /**
     * Loads a JSON file from the filesystem and deserializes it into the specified type.
     *
     * @param path           The filesystem path to the JSON file.
     * @param typeReference  A {@link TypeReference} representing the type to deserialize into.
     * @param <T>            The type of the object to return.
     * @return The deserialized object, or {@code null} if an error occurs.
     */
    public static <T> T load(String path, TypeReference<T> typeReference) {
        try (InputStream inputStream = new FileInputStream(path)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Could not load file: " + path + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the shared {@link ObjectMapper} instance used for JSON processing.
     *
     * @return The {@link ObjectMapper} instance.
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
