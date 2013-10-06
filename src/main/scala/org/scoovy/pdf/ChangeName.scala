package org.scoovy.pdf
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import java.io.File
import java.nio.file.Files
object ChangeName {
  def main(args:Array[String]) = {
    val folder = new File("""/Users/tomo7105/Documents/comics/""" + Setting.name + "/")
    folder.listFiles().foreach{f =>
      val name = f.getName()
      val newName = Setting.name + name.replaceAll("""\D""", "") + ".pdf" 
      println(f.toPath())
      println(newName)
      val newFile = new File(f.getParentFile(), newName)
      f.renameTo(newFile)
    }
  }
}
