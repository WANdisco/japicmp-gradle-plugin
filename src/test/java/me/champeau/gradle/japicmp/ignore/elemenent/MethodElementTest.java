package me.champeau.gradle.japicmp.ignore.elemenent;

import japicmp.model.JApiMethod;
import japicmp.model.JApiParameter;
import javassist.NotFoundException;
import me.champeau.gradle.japicmp.ignore.BaseTest;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import org.junit.Assert;
import org.junit.Test;

public class MethodElementTest extends BaseTest {
  @Test
  public void test() throws NotFoundException {
    String stringClassName = "java.lang.String";
    String classname = "com.test.TestClass";
    MethodElement methodElement = crateMethodElement("com.test.TestClass","test", stringClassName, new String[] {stringClassName});

    JApiMethod jApiMethod = createJApiMethod("test", classname, new String[] {stringClassName}, stringClassName);
    jApiMethod.addParameter(new JApiParameter(stringClassName));

    Assert.assertTrue(methodElement.process(jApiMethod));
  }
}
