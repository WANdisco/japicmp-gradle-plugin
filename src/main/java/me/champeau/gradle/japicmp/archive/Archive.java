package me.champeau.gradle.japicmp.archive;

import japicmp.cmp.JApiCmpArchive;
import me.champeau.gradle.japicmp.ignore.Parser;

import java.io.File;
import java.io.Serializable;

public class Archive implements Serializable {
  private final File file;
  private final Version version;
  private final String fileName;

  public Archive(File file, String fileName, String version) {
    this.file = file;
    this.fileName = fileName;
    this.version = new Version(version);
  }

  public static Archive fromJarFile(File file) {
    Parser.JarFileInfo fileInfo = Parser.parseJarFileInfo(file);
    return new Archive(file, fileInfo.getArchiveName(), fileInfo.getArchiveVersion());
  }

  public File getFile() {
    return file;
  }

  public Version getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return file.getName();
  }
  
  
  public String getFileName() {
    return fileName;
  }

  public JApiCmpArchive toJapicmpArchive() {
    return new JApiCmpArchive(file, version.toString());
  }
}
