package opentierlist.model.models;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import opentierlist.persistence.ResourceHolder;

/**
 * Self-explanatory
 * 
 * @author flynnz
 * @version 0.10
 * @since v1.7.0
 */
public class ImagePath {
    private String url;
    
    private static final String DEFAULT_IMAGE_RESOURCE = ResourceHolder.getDefaultTelementIcon();

    // for testing only
    private static final String DEFAULT_IMAGES_FOLDER = ResourceHolder.getDefaultImagesFolder();

    private ImagePath(String url) {
        this.url = url;
    }

    // for testing only
    public static ImagePath of(String path) {
    	Path jarPath = basePath();
    				// to make it work in the IDE
    	String base = jarPath.toString().contains("bin") 
    				? ".." + DEFAULT_IMAGES_FOLDER 
    				: DEFAULT_IMAGES_FOLDER;

        Path real = jarPath.resolve(base).resolve(path).normalize();
        
        if (path != null && Files.exists(real)) {
            return new ImagePath(real.toUri().toString());
        }
        return defaultResource();
    }
    
    public static ImagePath of(File file) {
    	if (file != null && file.exists()) {
    		return new ImagePath(file.toURI().toString());
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

    // for testing only
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
    
    public boolean exists() {
    	try {
			return Files.exists(Path.of(new URI(url)));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
    }
}
