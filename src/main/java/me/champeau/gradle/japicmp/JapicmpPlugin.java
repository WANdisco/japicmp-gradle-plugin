package me.champeau.gradle.japicmp;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Cedric Champeau
 */
public class JapicmpPlugin implements Plugin<Project> {
  public void apply(Project project) {
    project.getExtensions().getExtraProperties().set("JapicmpTask", me.champeau.gradle.japicmp.JapicmpTask.class);
  }
}
