package net.flynn.opentierlist.model.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * Self-explanatory
 * 
 * @author flynnz
 * @version 0.10
 * @since v1.7.0
 */
public class ImagePath {
  private URI uri;

  private static final String DEFAULT_IMAGE_RESOURCE = ResourceHolder.getDefaultTelementIcon();

  private ImagePath(URI uri) {
    this.uri = uri;
  }

  public static ImagePath of(File file) throws FileNotFoundException {
    if (file != null && file.exists()) {
      return new ImagePath(file.toURI());
    }
    return defaultResource();
  }

  public static ImagePath defaultResource() throws FileNotFoundException {
    URL url = ImagePath.class.getResource(DEFAULT_IMAGE_RESOURCE);
    if (url == null)
      throw new IllegalStateException("Resource missing: " + DEFAULT_IMAGE_RESOURCE);

    try {
      return new ImagePath(url.toURI());
    } catch (URISyntaxException e) {
      throw new FileNotFoundException();
    }
  }

  public String getUri() {
    return this.uri.toString();
  }

  public boolean exists() {
    return Files.exists(Path.of(uri));
  }

  // for testing only
  public static ImagePath of(String path) throws FileNotFoundException {
    Path jarPath = basePath();
    // to make it work in the IDE
    String tail = jarPath.toString().contains("bin")
        ? ".." + ResourceHolder.getDefaultImagesFolder()
        : ResourceHolder.getDefaultImagesFolder();

    Path realPath = jarPath.resolve(tail).resolve(path).normalize();

    if (path != null && Files.exists(realPath)) {
      return new ImagePath(realPath.toUri());
    }
    return defaultResource();
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
}
