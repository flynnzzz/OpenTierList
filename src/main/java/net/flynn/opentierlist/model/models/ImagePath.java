package net.flynn.opentierlist.model.models;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import net.flynn.opentierlist.persistence.ResourceHolder;

/**
 * Custom class to handle image resources
 * 
 * @author flynnz
 * @version 0.10
 * @since v1.7.0
 */
public class ImagePath {
  private final URI uri;

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

  public static ImagePath of(String uri) throws FileNotFoundException {

    try {
      return ImagePath.of(new URI(uri));
    }
    catch (FileNotFoundException | URISyntaxException _) {
      return defaultResource();
    }
  }

  public static ImagePath of(URI uri) throws FileNotFoundException {

    return ImagePath.of(new File(uri));

  }

  public static ImagePath defaultResource() {
    URL url = ImagePath.class.getResource(DEFAULT_IMAGE_RESOURCE);
    if (url == null)
      throw new IllegalStateException("Resource missing: " + DEFAULT_IMAGE_RESOURCE);
    try {
      return new ImagePath(url.toURI());
    } catch (URISyntaxException e) {
      System.err.println("--- Default resource not found, aborting ---");
      System.exit(-1);
      return null;
    }
  }

  public String getUri() {
    return this.uri.toString();
  }

  public boolean exists() {
    return Files.exists(Path.of(uri));
  }
}
