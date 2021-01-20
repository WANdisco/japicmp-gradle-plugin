package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
  @Test
  public void parserTest() {
    Element<?> element = Parser.parseElement("com.test.common.Constants.FIELD:java.lang.String");
    Element<?> element1 = Parser.parseElement("com.test.server.DummyStorage.getList(java.net.URI):List<java.lang.Long>");
    Element<?> element2 = Parser.parseElement("com.test.dto.Constructor(java.lang.String,java.lang.String,java.lang.String, java.lang.String):<init>");
    Element<?> element3 = Parser.parseElement("com.test.server.Server.test():com.test.server.Server");
    Element<?> element4 = Parser.parseElement("Constants.FIELD:java.lang.String");
    Element<?> element5 = Parser.parseElement("com.test.Class");

    Assert.assertTrue(element instanceof FieldElement);
    Assert.assertTrue(element1 instanceof MethodElement);
    Assert.assertTrue(element2 instanceof ConstructorElement);
    Assert.assertTrue(element3 instanceof MethodElement);
    Assert.assertTrue(element4 instanceof FieldElement);
    Assert.assertTrue(element5 instanceof ClassElement);

    FieldElement fieldElement = (FieldElement) element;
    MethodElement methodElement = (MethodElement) element1;
    ConstructorElement constructorElement = (ConstructorElement) element2;
    MethodElement methodWithoutArgs = (MethodElement) element3;
    FieldElement fieldWithoutPackage = (FieldElement) element4;
    ClassElement classElement = (ClassElement) element5;

    Assert.assertEquals(fieldElement.getFieldType(), "java.lang.String");
    Assert.assertEquals(fieldElement.getFieldName(), "FIELD");
    Assert.assertEquals(fieldElement.getClassName(), "Constants");
    Assert.assertEquals(fieldElement.getPackage(), "com.test.common");


    Assert.assertEquals(methodElement.getPackage(), "com.test.server");
    Assert.assertEquals(methodElement.getName(), "getList");
    Assert.assertEquals(methodElement.getReturnedType(), "List<java.lang.Long>");
    Assert.assertEquals(methodElement.getClassName(), "DummyStorage");
    Assert.assertEquals(methodElement.getArgs().get("arg0"), "java.net.URI");

    Assert.assertEquals(constructorElement.getPackage(), "com.test.dto");
    Assert.assertEquals(constructorElement.getName(), "Constructor");
    Assert.assertEquals(constructorElement.getClassName(), "Constructor");
    for (String argType : constructorElement.getArgs().values()) {
      Assert.assertEquals(argType, "java.lang.String");
    }

    Assert.assertEquals(methodWithoutArgs.getPackage(), "com.test.server");
    Assert.assertEquals(methodWithoutArgs.getName(), "test");
    Assert.assertEquals(methodWithoutArgs.getClassName(), "Server");
    Assert.assertNull(methodWithoutArgs.getArgs().get("arg0"));
    Assert.assertEquals(methodWithoutArgs.getReturnedType(), "com.test.server.Server");

    Assert.assertEquals(fieldWithoutPackage.getFieldType(), "java.lang.String");
    Assert.assertEquals(fieldWithoutPackage.getFieldName(), "FIELD");
    Assert.assertEquals(fieldWithoutPackage.getClassName(), "Constants");
    Assert.assertEquals(fieldWithoutPackage.getPackage(), "");

    Assert.assertEquals(classElement.getClassName(), "Class");
    Assert.assertEquals(classElement.getPackage(), "com.test");
  }
}
