package model.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Self-explanatory
 * 
 * @author flynnz
 * @version 0.00
 * @since v1.7.0
 */
public class ImagePath {
    private String url;
    private static final String DEFAULT_IMAGE_RESOURCE = "/default_icon.jpeg";
    private static final String DEFAULT_IMAGES_FOLDER = "/external/images/";

    private ImagePath(String url) {
        this.url = url;
    }

    public static ImagePath of(String path) {
        Path real = basePath().resolve(".." + DEFAULT_IMAGES_FOLDER).resolve(path).normalize();
        if (path != null && Files.exists(real)) {
            return new ImagePath(real.toUri().toString());
        }
        return defaultResource();
    }

    public static ImagePath defaultResource() {
        URL url = ImagePath.class.getResource(DEFAULT_IMAGE_RESOURCE);
        if (url == null)
            throw new IllegalStateException("Default image resource missing: " + DEFAULT_IMAGE_RESOURCE);

        return new ImagePath(url.toString());
    }

    public String getUrl() {
        return this.url;
    }

    private static Path basePath() {
        try {
            URI jarUri = ImagePath.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI();

            Path jarPath = Path.of(jarUri);
            return Files.isDirectory(jarPath) ? jarPath : jarPath.getParent();

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not determine application directory", e);
        }
    }
}

/*
        this.imagePath = (path.isEmpty() || !Files.exists(Path.of(path.get())))
                ? getClass().getResource(DEFAULT_ELEMENT_IMAGE_FILE).toString()
                : BASE_PATH.resolve(path.get()).normalize().toString();

*/
