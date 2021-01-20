package me.champeau.gradle.japicmp.ignore.entity;

import japicmp.model.JApiMethod;
import japicmp.model.JApiParameter;
import me.champeau.gradle.japicmp.ignore.BaseTest;
import me.champeau.gradle.japicmp.ignore.element.MethodElement;
import org.junit.Assert;
import org.junit.Test;

public class ChangeEntityTest extends BaseTest {

  @Test
  public void test() {
    String stringClassName = "java.lang.String";
    String integerClassName = "java.lang.Integer";
    String classname = "com.test.TestClass";
    MethodElement methodElement1 = crateMethodElement("com.test.TestClass","test", stringClassName, new String[] {stringClassName});
    MethodElement methodElement2 = crateMethodElement("com.test.TestClass","test", integerClassName, new String[] {stringClassName});

    JApiMethod jApiMethod1 = createJApiMethod("test", classname, new String[] {stringClassName}, stringClassName);
    jApiMethod1.addParameter(new JApiParameter(stringClassName));

    JApiMethod jApiMethod2 = createJApiMethod("test", classname, new String[] {stringClassName}, integerClassName);
    jApiMethod2.addParameter(new JApiParameter(stringClassName));

    ChangeMethodEntity changeMethodEntity = new ChangeMethodEntity("1.2", methodElement1, methodElement2);

    Assert.assertTrue(changeMethodEntity.validate(jApiMethod1, jApiMethod2));

  }
}
