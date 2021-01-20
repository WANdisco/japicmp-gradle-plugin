package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;

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
    int lastDot = s.lastIndexOf(".");
    if (lastDot == -1) {
      return new String[] {"", s};
    }

    return new String[] {s.substring(0, lastDot), s.substring(lastDot + 1)};
  }
}
