package org.scoovy.pdf
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import java.io.File
object FileSample {
  def main(args:Array[String]):Unit = {
    val file = new File("/Users/tomo7105/Downloads/kimi/Kimi ni Todoke 08")
    file.listFiles().sorted.foreach{println}
  }
}