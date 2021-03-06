package me.champeau.gradle.japicmp.ignore.element;

import japicmp.model.JApiMethod;
import japicmp.model.JApiParameter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodElement extends Element<JApiMethod> {
  private final String returnedType;
  private final String name;
  private final Map<String, String> args;

  public MethodElement(String fullPackageName, String className, String returnedType, String name, Map<String, String> args) {
    super(fullPackageName, className);
    this.returnedType = returnedType;
    this.name = name;
    this.args = args;
  }

  public String getReturnedType() {
    return returnedType;
  }

  public String getName() {
    return name;
  }

  @Override
  public String doGetIdentifier() {
    return getName() + "(" + String.join(",", args.values()) + ")" + returnedType;
  }

  public Map<String, String> getArgs() {
    return args;
  }

  @Override
  public boolean process(JApiMethod method) {
    if (!validateMethodName(method.getName())) return false;
    if (!validateArgs(method.getParameters())) return false;
    if (!validateReturnType(method.getReturnType().getNewReturnType())
        && !validateReturnType(method.getReturnType().getOldReturnType())) return false;


    return true;
  }

  private boolean validateMethodName(String methodName) {
    return Objects.equals(name, methodName);
  }

  private boolean validateReturnType(String returnedType) {
    return Objects.equals(this.returnedType, returnedType);
  }

  private boolean validateArgs(List<JApiParameter> parameters) {
    return ElementUtil.validateArgs(parameters, args);
  }


  public static String extractIdentifier(JApiMethod jApiMethod, boolean isOld) {
    return jApiMethod.getjApiClass().getFullyQualifiedName() + ":" +
        jApiMethod.getName() +
        "(" + jApiMethod.getParameters().stream().map(JApiParameter::getType).collect(Collectors.joining(",")) + ")" +
        (isOld ? jApiMethod.getReturnType().getOldReturnType() : jApiMethod.getReturnType().getNewReturnType());
  }
}
