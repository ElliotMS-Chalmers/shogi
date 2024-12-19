package util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResolver {

    public static Path getAppDataPath() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();
        Path path;
        if (os.contains("win")) {
            path = Paths.get(userHome, "AppData", "Local", "Shogi");
        } else {
            path = Paths.get(userHome, ".config", "Shogi");
        }
        return path;
    }

    public static Path getAppDataPath(String fileName) {
        return Paths.get(getAppDataPath().toString(), fileName);
    }

}
