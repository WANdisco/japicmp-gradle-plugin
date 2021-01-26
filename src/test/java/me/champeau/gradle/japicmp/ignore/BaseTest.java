package me.champeau.gradle.japicmp.ignore;

import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiClassType;
import japicmp.model.JApiMethod;
import japicmp.util.Optional;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
  protected final ClassPool classPool = new ClassPool(ClassPool.getDefault());
  private final JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(new JarArchiveComparatorOptions());


  public MethodElement crateMethodElement(String classname, String methodName, String returnedType, String[] args) {
    String[] strings = Parser.splitByLastDotChar(classname);
    Map<String, String> argsMap = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      argsMap.put("arg" + i, arg);
    }
    return new MethodElement(strings[0], strings[1], returnedType, methodName, argsMap);
  }

  public JApiMethod createJApiMethod(String methodName, String classname, String[] args, String returnType) {
    CtClass[] ctClasses = Arrays.stream(args).map(this::safeGetClass).toArray(CtClass[]::new);
    CtClass ctClass = safeGetClass(classname);
    CtMethod method = new CtMethod(safeGetClass(returnType), methodName, ctClasses, ctClass);
    JApiClass jApiClass = new JApiClass(
        jarArchiveComparator,
        classname,
        Optional.of(ctClass), Optional.absent(),
        JApiChangeStatus.MODIFIED,
        new JApiClassType(Optional.of(JApiClassType.ClassType.CLASS), Optional.absent(), JApiChangeStatus.UNCHANGED)
    );
    return new JApiMethod(
        jApiClass,
        methodName,
        JApiChangeStatus.UNCHANGED,
        Optional.of(method),
        Optional.absent(),
        jarArchiveComparator
    );
  }

  private CtClass safeGetClass(String name) {
    try {
      return classPool.get(name);
    } catch (NotFoundException e) {
      return classPool.makeClass(name);
    }
  }
}
