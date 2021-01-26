package me.champeau.gradle.japicmp.ignore;

import me.champeau.gradle.japicmp.ignore.element.ClassElement;
import me.champeau.gradle.japicmp.ignore.element.ConstructorElement;
import me.champeau.gradle.japicmp.ignore.element.Element;
import me.champeau.gradle.japicmp.ignore.element.FieldElement;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

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

  @Test
  public void testVersionExtracter() {
    File folder = new File("path/to/file");
    Assert.assertEquals("1.0", Parser.tryExtractVersion(new File(folder, "test.jar")));
    Assert.assertEquals("2.0", Parser.tryExtractVersion(new File(folder, "test-2.0.jar")));
    Assert.assertEquals("2.0-SNAPSHOT", Parser.tryExtractVersion(new File(folder, "test-2.0-SNAPSHOT.jar")));
    Assert.assertEquals("2.0.2.3.4", Parser.tryExtractVersion(new File(folder, "test-2.0.2.3.4.jar")));
    Assert.assertEquals("2", Parser.tryExtractVersion(new File(folder, "test-2.jar")));
    Assert.assertEquals("1.0", Parser.tryExtractVersion(new File(folder, "test-.jar")));
    Assert.assertEquals("1.0", Parser.tryExtractVersion(new File(folder, "test-SNAPSHOT.jar")));
    Assert.assertEquals("1.0", Parser.tryExtractVersion(new File(folder, "test-2.-SNAPSHOT.jar")));
  }

  @Test
  public void testJarInfoParser() {
    Parser.JarFileInfo fileInfo0 = Parser.parseJarFileInfo(new File("test.jar"));
    Assert.assertEquals("test", fileInfo0.getArchiveName());
    Assert.assertNull(fileInfo0.getArchiveVersion());
    Assert.assertNull(fileInfo0.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo0.getArchiveExtension());

    Parser.JarFileInfo fileInfo = Parser.parseJarFileInfo(new File("test-2.1.jar"));
    Assert.assertEquals("test", fileInfo.getArchiveName());
    Assert.assertEquals("2.1", fileInfo.getArchiveVersion());
    Assert.assertNull(fileInfo.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo.getArchiveExtension());

    Parser.JarFileInfo fileInfo1 = Parser.parseJarFileInfo(new File("test-2-SNAPSHOT.jar"));
    Assert.assertEquals("test", fileInfo1.getArchiveName());
    Assert.assertEquals("2-SNAPSHOT", fileInfo1.getArchiveVersion());
    Assert.assertNull(fileInfo1.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo1.getArchiveExtension());

    Parser.JarFileInfo fileInfo2 = Parser.parseJarFileInfo(new File("hadoop-hdfs-3.3.0-SNAPSHOT-tests.jar"));
    Assert.assertEquals("hadoop-hdfs", fileInfo2.getArchiveName());
    Assert.assertEquals("3.3.0-SNAPSHOT", fileInfo2.getArchiveVersion());
    Assert.assertEquals("tests", fileInfo2.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo2.getArchiveExtension());

    Parser.JarFileInfo fileInfo3 = Parser.parseJarFileInfo(new File("hadoop-hdfs-3.3.0-tests.jar"));
    Assert.assertEquals("hadoop-hdfs", fileInfo3.getArchiveName());
    Assert.assertEquals("3.3.0", fileInfo3.getArchiveVersion());
    Assert.assertEquals("tests", fileInfo3.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo3.getArchiveExtension());

    Parser.JarFileInfo fileInfo4 = Parser.parseJarFileInfo(new File("test-hdfs.jar"));
    Assert.assertEquals("test", fileInfo4.getArchiveName());
    Assert.assertNull(fileInfo4.getArchiveVersion());
    Assert.assertEquals("hdfs", fileInfo4.getArchiveClassifier());
    Assert.assertEquals("jar", fileInfo4.getArchiveExtension());
  }
}
