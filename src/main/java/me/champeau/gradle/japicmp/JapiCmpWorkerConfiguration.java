/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.champeau.gradle.japicmp;

import me.champeau.gradle.japicmp.filters.FilterConfiguration;
import me.champeau.gradle.japicmp.report.RichReport;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class JapiCmpWorkerConfiguration implements Serializable {
  protected final boolean includeSynthetic;
  protected final boolean ignoreMissingClasses;
  protected final List<String> packageIncludes;
  protected final List<String> packageExcludes;
  protected final List<String> classIncludes;
  protected final List<String> classExcludes;
  protected final List<String> methodIncludes;
  protected final List<String> methodExcludes;
  protected final List<String> fieldIncludes;
  protected final List<String> fieldExcludes;
  protected final List<String> annotationIncludes;
  protected final List<String> annotationExcludes;
  protected final List<FilterConfiguration> includeFilters;
  protected final List<FilterConfiguration> excludeFilters;
  protected final List<Archive> oldClasspath;
  protected final List<Archive> newClasspath;
  protected final List<Archive> oldArchives;
  protected final List<Archive> newArchives;
  protected final boolean onlyModified;
  protected final boolean onlyBinaryIncompatibleModified;
  protected final boolean failOnSourceIncompatibility;
  protected final String accessModifier;
  protected final File xmlOutputFile;
  protected final File htmlOutputFile;
  protected final File txtOutputFile;
  protected final boolean failOnModification;
  protected final File buildDir;
  protected final RichReport richReport;
    protected final File compatibilityChangesFilterFile;

  public JapiCmpWorkerConfiguration(boolean includeSynthetic,
                                      boolean ignoreMissingClasses,
                                      List<String> packageIncludes,
                                      List<String> packageExcludes,
                                      List<String> classIncludes,
                                      List<String> classExcludes,
                                      List<String> methodIncludes,
                                      List<String> methodExcludes,
                                      List<String> fieldIncludes,
                                      List<String> fieldExcludes,
                                      List<String> annotationIncludes,
                                      List<String> annotationExcludes,
                                      List<FilterConfiguration> includeFilters,
                                      List<FilterConfiguration> excludeFilters,
                                      List<Archive> oldClasspath,
                                      List<Archive> newClasspath,
                                      List<Archive> oldArchives,
                                      List<Archive> newArchives,
                                      boolean onlyModified,
                                      boolean onlyBinaryIncompatibleModified,
                                      boolean failOnSourceIncompatibility,
                                      String accessModifier,
                                      File xmlOutputFile,
                                      File htmlOutputFile,
                                      File txtOutputFile,
                                      boolean failOnModification,
                                      File buildDir,
                                      RichReport richReport,
                                      File compatibilityChangesFilterFile) {
        this.includeSynthetic = includeSynthetic;
        this.ignoreMissingClasses = ignoreMissingClasses;
        this.packageIncludes = packageIncludes;
        this.packageExcludes = packageExcludes;
        this.classIncludes = classIncludes;
        this.classExcludes = classExcludes;
        this.methodIncludes = methodIncludes;
        this.methodExcludes = methodExcludes;
        this.fieldIncludes = fieldIncludes;
        this.fieldExcludes = fieldExcludes;
        this.annotationIncludes = annotationIncludes;
        this.annotationExcludes = annotationExcludes;
        this.includeFilters = includeFilters;
        this.excludeFilters = excludeFilters;
        this.oldClasspath = oldClasspath;
        this.newClasspath = newClasspath;
        this.oldArchives = oldArchives;
        this.newArchives = newArchives;
        this.onlyModified = onlyModified;
        this.onlyBinaryIncompatibleModified = onlyBinaryIncompatibleModified;
        this.failOnSourceIncompatibility = failOnSourceIncompatibility;
        this.accessModifier = accessModifier;
        this.xmlOutputFile = xmlOutputFile;
        this.htmlOutputFile = htmlOutputFile;
        this.txtOutputFile = txtOutputFile;
        this.compatibilityChangesFilterFile = compatibilityChangesFilterFile;
    this.failOnModification = failOnModification | failOnSourceIncompatibility;
    this.buildDir = buildDir;
    this.richReport = richReport;
  }

}
