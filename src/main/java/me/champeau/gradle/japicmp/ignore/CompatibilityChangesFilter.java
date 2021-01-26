package me.champeau.gradle.japicmp.ignore;


import me.champeau.gradle.japicmp.archive.Diff;
import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import me.champeau.gradle.japicmp.ignore.entity.ChangeConstructorEntity;
import me.champeau.gradle.japicmp.ignore.entity.ChangeFieldEntity;
import me.champeau.gradle.japicmp.ignore.entity.ChangeMethodEntity;
import me.champeau.gradle.japicmp.ignore.entity.ClassDescriptor;
import me.champeau.gradle.japicmp.ignore.entity.Entity;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;
import me.champeau.gradle.japicmp.ignore.entity.RemoveClassEntity;
import me.champeau.gradle.japicmp.ignore.entity.RemoveConstructorEntity;
import me.champeau.gradle.japicmp.ignore.entity.RemoveFieldEntity;
import me.champeau.gradle.japicmp.ignore.entity.RemoveMethodEntity;
import me.champeau.gradle.japicmp.ignore.processor.ClassProcessor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompatibilityChangesFilter {
  private final List<Project> projects;

  public CompatibilityChangesFilter(File configFile) {
    Yaml yaml = new Yaml();
    HashMap<String, List<Map<String, String>>> objects = null;
    try {
      objects = yaml.loadAs(new FileInputStream(configFile), HashMap.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    projects = fillProjects(objects);
  }

  private List<Project> fillProjects(HashMap<String, List<Map<String, String>>> objects) {
    List<Project> result = new ArrayList<>();
    if (objects != null) {
      for (Map.Entry<String, List<Map<String, String>>> projectEntry : objects.entrySet()) {
        Project project = new Project(projectEntry.getKey());
        result.add(project);
        List<Map<String, String>> value = projectEntry.getValue();
        if (value == null || value.isEmpty()) continue;
        for (Map<String, String> entity : value) {
          project.add(extractEntity(entity));
        }
      }
    }
    return result;
  }


  public void filterChanges(Diff diff) {
    List<ClassDescriptor> collect = projects.stream()
        .filter(diff::containsProject)
        .map(Project::classes)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    ClassProcessor classProcessor = new ClassProcessor(new EntityManager(collect));
    diff.forEach(diffInfo ->
        classProcessor.process(
            diffInfo.getClasses(),
            diffInfo.getVersionsRange()
        )
    );

  }

  private Entity extractEntity(Map<String, String> map) {
    if (map.containsKey("remove")) {
      return remove(map);
    } else if (map.containsKey("change")) {
      return change(map);
    }
    throw new IllegalArgumentException("");
  }

  private Entity remove(Map<String, String> map) {
    String version = null;
    String entity = null;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      switch (entry.getKey()) {
        case "version":
          version = entry.getValue();
          break;
        case "entity":
          entity = entry.getValue();
          break;
      }
    }
    if (version == null) {
      throw new NullPointerException("Missing version property in remove element");
    } else if (entity == null) {
      throw new NullPointerException("Missing entity property in change element");
    }
    Element<?> element = Parser.parseElement(entity);

    if (element instanceof FieldElement) {
      return new RemoveFieldEntity(version, (FieldElement) element);
    } else if (element instanceof MethodElement) {
      return new RemoveMethodEntity(version, (MethodElement) element);
    } else if (element instanceof ConstructorElement) {
      return new RemoveConstructorEntity(version, (ConstructorElement) element);
    } else if (element instanceof ClassElement) {
      return new RemoveClassEntity(version, (ClassElement) element);
    }

    return null;
  }

  private Entity change(Map<String, String> map) {
    String version = null;
    String prevEntity = null;
    String newEntity = null;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      switch (entry.getKey()) {
        case "version":
          version = entry.getValue();
          break;
        case "prevEntity":
          prevEntity = entry.getValue();
          break;
        case "newEntity":
          newEntity = entry.getValue();
          break;
      }
    }
    if (version == null) {
      throw new NullPointerException("Missing version property in change element");
    } else if (prevEntity == null) {
      throw new NullPointerException("Missing prevEntity property in change element");
    } else if (newEntity == null) {
      throw new NullPointerException("Missing newEntity property in change element");
    }

    Element<?> prevElement = Parser.parseElement(prevEntity);
    Element<?> newElement = Parser.parseElement(newEntity);


    if (prevElement instanceof FieldElement) {
      return new ChangeFieldEntity(version, (FieldElement) prevElement, (FieldElement) newElement);
    } else if (prevElement instanceof MethodElement) {
      return new ChangeMethodEntity(version, (MethodElement) prevElement, (MethodElement) newElement);
    } else if (prevElement instanceof ConstructorElement) {
      return new ChangeConstructorEntity(version, (ConstructorElement) prevElement, (ConstructorElement) newElement);
    }

    return null;
  }
}