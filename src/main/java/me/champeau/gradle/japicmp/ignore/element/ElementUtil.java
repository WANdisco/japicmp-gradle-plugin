package me.champeau.gradle.japicmp.ignore.element;

import japicmp.model.JApiParameter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ElementUtil {

  public static boolean validateArgs(List<JApiParameter> parameters, Map<String, String> args) {
    if (parameters.size() != args.size()) return false;

    int i = 0;
    //Performance
    //Do not change to indexed loop because of parameters is LinkedList
    for (JApiParameter parameter : parameters) {
      String argType = args.get("arg" + i++);
      if (!Objects.equals(argType, parameter.getType())) {
        return false;
      }
    }

    return true;
  }
}
