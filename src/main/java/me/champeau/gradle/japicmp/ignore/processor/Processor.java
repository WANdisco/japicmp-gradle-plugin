package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiClass;
import me.champeau.gradle.japicmp.archive.VersionsRange;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;

import java.util.List;

public class Processor {
  private final ClassMutator classMutator;
  private final ClassProcessor classProcessor;
  private final MethodProcessor methodProcessor;
  private final FieldProcessor fieldProcessor;
  private final ConstructorProcessor constructorProcessor;

  public Processor(EntityManager manager) {
    classMutator = new ClassMutator();
    classProcessor = new ClassProcessor(classMutator, manager);
    methodProcessor = new MethodProcessor(classMutator, manager);
    fieldProcessor = new FieldProcessor(classMutator, manager);
    constructorProcessor = new ConstructorProcessor(classMutator, manager);
  }

  public void process(List<JApiClass> classes, VersionsRange versions) {
    for (JApiClass clazz : classes) {
      classProcessor.process(clazz, versions);
      methodProcessor.process(clazz.getMethods(), versions);
      fieldProcessor.process(clazz.getFields(), versions);
      constructorProcessor.process(clazz.getConstructors(), versions);
      classMutator.tryClearClass(clazz);
    }
  }
}
