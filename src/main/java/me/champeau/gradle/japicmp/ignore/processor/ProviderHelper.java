package me.champeau.gradle.japicmp.ignore.processor;

import japicmp.model.JApiClass;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import me.champeau.gradle.japicmp.ignore.entity.Provider;

public class ProviderHelper {

  public static Provider.MutableProvider<JApiClass> createDefaultClassProvider() {
    return new Provider.OnlyRemoveElementProvider<JApiClass>() {
      @Override
      public String getIdentifier() {
        return ClassElement.extractIdentifier(getRemoveElement());
      }
    };
  }

  public static Provider.MutableProvider<JApiConstructor> createDefaultConstructorProvider() {
    return new Provider.MutableProviderImpl<JApiConstructor>() {
      @Override
      public String getIdentifier() {
        Pair<JApiConstructor> changeElement = getChangeElement();
        if (changeElement != null) {
          return ConstructorElement.extractIdentifier(changeElement.getFirst()) + ":"
              + ConstructorElement.extractIdentifier(changeElement.getSecond());
        } else {
          return ConstructorElement.extractIdentifier(getRemoveElement());
        }
      }
    };
  }

  public static Provider.MutableProvider<JApiField> createDefaultFieldProvider() {
    return new Provider.MutableProviderImpl<JApiField>() {
      @Override
      public String getIdentifier() {
        Pair<JApiField> changeElement = getChangeElement();
        if (changeElement != null) {
          return FieldElement.extractIdentifier(changeElement.getFirst(), true) + ":"
              + FieldElement.extractIdentifier(changeElement.getSecond(), false);
        } else {
          return FieldElement.extractIdentifier(getRemoveElement(), true);
        }
      }
    };
  }

  public static Provider.MutableProvider<JApiMethod> createDefaultMethodProvider() {
    return new Provider.MutableProviderImpl<JApiMethod>() {
      @Override
      public String getIdentifier() {
        Pair<JApiMethod> changeElement = getChangeElement();
        if (changeElement != null) {
          return MethodElement.extractIdentifier(changeElement.getFirst(), true) + ":"
              + MethodElement.extractIdentifier(changeElement.getSecond(), false);
        } else {
          return MethodElement.extractIdentifier(getRemoveElement(), true);
        }
      }
    };
  }
}
