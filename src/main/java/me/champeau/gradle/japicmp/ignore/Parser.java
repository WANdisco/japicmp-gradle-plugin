package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.archive.Version;
import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Parser {

  public static Element<?> parseElement(String rawElement) {
    boolean isMethod = rawElement.indexOf('(') != -1;
    if (isMethod) {
      return parseCallableElement(rawElement);
    } else if (!rawElement.contains(":")) {
      return parseClassElement(rawElement);
    } else {
      return parseFieldElement(rawElement);
    }
  }

  private static Element<?> parseCallableElement(String rawElement) {
    int startArgs = rawElement.indexOf('(');
    String returnedType = rawElement.split(":")[1];
    boolean isConstructor = Objects.equals(returnedType, "<init>");
    String rawMethodElement = rawElement.substring(0, startArgs);
    String[] packageClassNameAndMethodName = splitByLastDotChar(rawMethodElement);
    Map<String, String> args = args(rawElement.substring(startArgs + 1, rawElement.indexOf(")")));
    if (isConstructor) {
      return new ConstructorElement(
          packageClassNameAndMethodName[0],
          packageClassNameAndMethodName[1],
          args
      );
    } else {
      String[] packageAndClassName = splitByLastDotChar(packageClassNameAndMethodName[0]);
      return new MethodElement(
          packageAndClassName[0].trim(),
          packageAndClassName[1].trim(),
          returnedType.trim(),
          packageClassNameAndMethodName[1].trim(),
          args
      );
    }
  }

  private static Element<?> parseFieldElement(String rawElement) {
    String[] fieldInfoAndType = rawElement.split(":");
    String[] strings = splitByLastDotChar(fieldInfoAndType[0]);
    String[] packageAndClassName = splitByLastDotChar(strings[0]);

    return new FieldElement(
        packageAndClassName[0].trim(),
        packageAndClassName[1].trim(),
        fieldInfoAndType[1].trim(),
        strings[1].trim()
    );
  }

  private static Element<?> parseClassElement(String rawElement) {
    String[] strings = splitByLastDotChar(rawElement);
    return new ClassElement(strings[0], strings[1]);
  }

  private static Map<String, String> args(String rawArgs) {
    Map<String, String> result = new HashMap<>();
    if (rawArgs.trim().isEmpty()) return result;

    String[] args = rawArgs.split(",");
    for (int i = 0; i < args.length; i++) {
      String argType = args[i];
      result.put("arg" + i, argType.trim());
    }
    return result;
  }

  public static String[] splitByLastDotChar(String s) {
    return splitByLastChar(s, '.');
  }

  public static String[] splitByLastMinusChar(String s) {
    return splitByLastChar(s, '-');
  }

  public static String[] splitByLastChar(String s, char c) {
    int lastDot = s.lastIndexOf(c);
    if (lastDot == -1) {
      return new String[] {"", s};
    }

    return new String[] {s.substring(0, lastDot), s.substring(lastDot + 1)};
  }

  public static String tryExtractVersion(File file) {
    JarFileInfo fileInfo = new JarFileInfo(file);
    String archiveVersion = fileInfo.getArchiveVersion();
    return archiveVersion != null ? archiveVersion : "1.0";
  }

  //${archiveBaseName}-${archiveAppendix}-${archiveVersion}-${archiveClassifier}.${archiveExtension}
  public static class JarFileInfo {
    private final String archiveName; // == ${archiveBaseName}-${archiveAppendix}
    private final String archiveVersion;
    private final String archiveClassifier;
    private final String archiveExtension;

    public JarFileInfo(File file) {
      String[] strings = splitByLastDotChar(file.getName());
      archiveExtension = strings[1];

      String fileName = strings[0];

      String[] nameVersionAndClassifier = splitByLastMinusChar(fileName);
      if (Version.checkIsVersion(nameVersionAndClassifier[1])) {
        archiveName = nameVersionAndClassifier[0];
        archiveVersion = nameVersionAndClassifier[1];
        archiveClassifier = null;
      } else {
        if (nameVersionAndClassifier[0].isEmpty()) {
          archiveName = fileName;
          archiveVersion = null;
          archiveClassifier = null;
        } else {
          archiveClassifier = nameVersionAndClassifier[1];
          String[] nameAndVersion = splitByLastMinusChar(nameVersionAndClassifier[0]);
          if (Version.checkIsVersion(nameAndVersion[1])) {
            archiveVersion = nameAndVersion[1];
            archiveName = nameAndVersion[0];
          } else {
            archiveVersion = null;
            archiveName = nameVersionAndClassifier[0];
          }
        }
      }
    }

    public String getArchiveName() {
      return archiveName;
    }

    public String getArchiveVersion() {
      return archiveVersion;
    }

    public String getArchiveClassifier() {
      return archiveClassifier;
    }

    public String getArchiveExtension() {
      return archiveExtension;
    }
  }
}
