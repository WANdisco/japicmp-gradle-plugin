package me.champeau.gradle.japicmp.ignore.element;

import japicmp.model.JApiConstructor;
import japicmp.model.JApiParameter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConstructorElement extends Element<JApiConstructor> {
  private final String name;
  private final Map<String, String> args;

  public ConstructorElement(String fullPackageName, String className, Map<String, String> args) {
    super(fullPackageName, className);
    this.name = className;
    this.args = args;
  }

  public String getName() {
    return name;
  }

  public Map<String, String> getArgs() {
    return args;
  }

  @Override
  public String doGetIdentifier() {
    return name + "(" + String.join(",", args.values()) + ")";
  }

  @Override
  public boolean process(JApiConstructor constructor) {
    if (!validateName(constructor.getName())) return false;
    if (!validateArgs(constructor.getParameters())) return false;

    return true;
  }

  private boolean validateArgs(List<JApiParameter> parameters) {
    return ElementUtil.validateArgs(parameters, args);
  }

  private boolean validateName(String name) {
    return Objects.equals(this.name, name);
  }

  public static String extractIdentifier(JApiConstructor constructor) {
    return constructor.getjApiClass().getFullyQualifiedName() + ":" +
        constructor.getName() +
        constructor.getParameters().stream()
            .map(JApiParameter::getType)
            .collect(Collectors.joining());
  }
}
