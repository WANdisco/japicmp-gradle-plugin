package me.champeau.gradle.japicmp.ignore;


import japicmp.model.JApiClass;
import me.champeau.gradle.japicmp.archive.ChangeInfo;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.entity.ChangeEntity;
import me.champeau.gradle.japicmp.ignore.entity.Entity;
import me.champeau.gradle.japicmp.ignore.entity.EntityManager;
import me.champeau.gradle.japicmp.ignore.entity.RemoveEntity;
import me.champeau.gradle.japicmp.ignore.processor.Processor;
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
  private final List<DeclaredModule> declaredModules;

  @SuppressWarnings("unchecked")
  public CompatibilityChangesFilter(File configFile) {
    Yaml yaml = new Yaml();
    HashMap<String, List<Map<String, String>>> objects = null;
    try {
      objects = yaml.loadAs(new FileInputStream(configFile), HashMap.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    declaredModules = fillModules(objects);
  }

  private List<DeclaredModule> fillModules(HashMap<String, List<Map<String, String>>> objects) {
    List<DeclaredModule> result = new ArrayList<>();
    if (objects != null) {
      for (Map.Entry<String, List<Map<String, String>>> projectEntry : objects.entrySet()) {
        result.add(parseAsModule(projectEntry.getKey(), projectEntry.getValue()));
      }
    }
    return result;
  }

  private DeclaredModule parseAsModule(String name, List<Map<String, String>> value) {
    DeclaredModule module = new DeclaredModule(name);
    if (value != null && !value.isEmpty()) {
      for (Map<String, String> entity : value) {
        module.add(extractEntity(entity));
      }
    }
    return module;
  }


  public List<JApiClass> filterChanges(ChangeInfo info) {
    List<Entity<?>> collect = declaredModules.stream()
        .filter(declaredModule -> info.isCorrectName(declaredModule.name))
        .map(DeclaredModule::getEntities)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    Processor processor = new Processor(new EntityManager(collect));
    return processor.process(info.classes(), info.getVersionsRange());
  }

  private Entity<?> extractEntity(Map<String, String> map) {
    if (map.containsKey("remove")) {
      return remove(map);
    } else if (map.containsKey("change")) {
      return change(map);
    }
    throw new IllegalArgumentException("");
  }

  private Entity<?> remove(Map<String, String> map) {
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

    return new RemoveEntity<>(version, element);
  }

  @SuppressWarnings("rawtypes")
  private Entity<?> change(Map<String, String> map) {
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

    return new ChangeEntity(version, prevElement, newElement);
  }
}
