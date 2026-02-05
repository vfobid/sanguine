package sanguine.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for reading in a configured file of cards for Sanguine.
 */
public interface FileReader {
  /**
   * Retrieves each line of the file from the given destination, and returns it as a list of
   * Strings.
   *
   * @param filePath the path to the file as a String.
   * @return a list of Strings with each entry being a line from the file.
   */
  static List<String> getLines(String filePath) {
    String[] parts = filePath.split("[/\\\\]");

    String computerPath = String.join(File.separator, parts);
    File file = new File(computerPath);
    List<String> lines = new ArrayList<>();
    try {
      lines = Files.readAllLines(file.toPath());
    } catch (NoSuchFileException e) {
      System.out.println("File not found.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }
}
