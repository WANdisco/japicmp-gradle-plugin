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

  public static Provider<JApiClass> createClassProvider(JApiClass removedClass) {
    return new Provider.OnlyRemoveElementProvider<JApiClass>(removedClass) {
      @Override
      public String getIdentifier() {
        return ClassElement.extractIdentifier(getRemoveElement());
      }
    };
  }

  public static Provider<JApiConstructor> createRemoveConstructorProvider(JApiConstructor removeElement) {
    return new Provider.OnlyRemoveElementProvider<JApiConstructor>(removeElement) {
      @Override
      public String getIdentifier() {
        return ConstructorElement.extractIdentifier(getRemoveElement());
      }
    };
  }

  public static Provider<JApiConstructor> createChangeConstructorProvider(
      JApiConstructor fromChangeElement,
      JApiConstructor toChangeElement) {
    return new Provider.OnlyChangeElementProvider<JApiConstructor>(fromChangeElement, toChangeElement) {
      @Override
      public String getIdentifier() {
        Pair<JApiConstructor> changeElement = getChangeElement();
        return ConstructorElement.extractIdentifier(changeElement.getFirst()) + ":" +
            ConstructorElement.extractIdentifier(changeElement.getSecond());
      }
    };
  }

  public static Provider<JApiField> createRemoveFieldProvider(JApiField removeElement) {
    return new Provider.OnlyRemoveElementProvider<JApiField>(removeElement) {
      @Override
      public String getIdentifier() {
        return FieldElement.extractIdentifier(getRemoveElement(), true);
      }
    };
  }

  public static Provider<JApiField> createChangeFieldProvider(
      JApiField fromChangeElement,
      JApiField toChangeElement) {
    return new Provider.OnlyChangeElementProvider<JApiField>(fromChangeElement, toChangeElement) {
      @Override
      public String getIdentifier() {
        Pair<JApiField> changeElement = getChangeElement();
        return FieldElement.extractIdentifier(changeElement.getFirst(), true) + ":"
            + FieldElement.extractIdentifier(changeElement.getSecond(), false);
      }
    };
  }

  public static Provider<JApiMethod> createRemoveMethodProvider(JApiMethod removeElement) {
    return new Provider.OnlyRemoveElementProvider<JApiMethod>(removeElement) {
      @Override
      public String getIdentifier() {
        return MethodElement.extractIdentifier(getRemoveElement(), true);
      }
    };
  }

  public static Provider<JApiMethod> createChangeMethodProvider(
      JApiMethod fromChangeElement,
      JApiMethod toChangeElement) {
    return new Provider.OnlyChangeElementProvider<JApiMethod>(fromChangeElement, toChangeElement) {
      @Override
      public String getIdentifier() {
        Pair<JApiMethod> changeElement = getChangeElement();
        return MethodElement.extractIdentifier(changeElement.getFirst(), true) + ":"
            + MethodElement.extractIdentifier(changeElement.getSecond(), false);
      }
    };
  }
}
