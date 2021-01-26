package me.champeau.gradle.japicmp.archive;

import org.junit.Assert;
import org.junit.Test;

public class VersionTest {

  @Test
  public void versionTest() {
    testVersion("2.1");
    testVersion("2.1.1");
    testVersion("2.10.10");
    testVersion("2.1.1.1");
  }

  private static Version testVersion(String version) {
    Version v = new Version(version);
    Assert.assertEquals(version, v.toString());
    return v;
  }

  @Test
  public void versionEqualsTest() {
    Assert.assertEquals(testVersion("2.1"), testVersion("2.1"));
    Assert.assertEquals(testVersion("2.1.10"), testVersion("2.1.10"));
    Assert.assertEquals(testVersion("2.1.10.3"), testVersion("2.1.10.3"));
  }

  @Test
  public void versionCompareTest() {
    Assert.assertEquals(-1, testVersion("2.1").compareTo(testVersion("2.2")));
    Assert.assertEquals(0, testVersion("2.1").compareTo(testVersion("2.1")));
    Assert.assertEquals(1, testVersion("3.1").compareTo(testVersion("2.1")));

    Assert.assertEquals(-1, testVersion("2.1.10").compareTo(testVersion("2.2.9")));
    Assert.assertEquals(1, testVersion("2.2.11").compareTo(testVersion("2.2.10")));
    Assert.assertEquals(0, testVersion("2.2.11").compareTo(testVersion("2.2.11")));

    Assert.assertEquals(0, testVersion("2.2.11.0").compareTo(testVersion("2.2.11.0")));
    Assert.assertEquals(0, testVersion("2.2.11.0").compareTo(testVersion("2.2.11")));
    Assert.assertEquals(0, testVersion("2.2.11.10").compareTo(testVersion("2.2.11")));

    Assert.assertEquals(1, testVersion("3.2.11.0").compareTo(testVersion("2.2.11.0")));
    Assert.assertEquals(1, testVersion("2.3.11.0").compareTo(testVersion("2.2.11.0")));
    Assert.assertEquals(1, testVersion("2.2.12.0").compareTo(testVersion("2.2.11.0")));
    Assert.assertEquals(1, testVersion("2.2.11.1").compareTo(testVersion("2.2.11.0")));
  }
}
