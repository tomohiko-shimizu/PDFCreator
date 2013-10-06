package org.scoovy.pdf
import java.io.File
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.sys.process._
import java.nio.file.SimpleFileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.StandardCopyOption
object UnRarSample {
  def main(args:Array[String]):Unit = {
    val fname = """/Users/tomo7105/Documents/comics/""" + Setting.name + "/" 
    val start = new File(fname)
    var list = List[File]()
    val fileListerner = new SimpleFileVisitor[Path](){
      override def visitFile(path:Path, attribute:BasicFileAttributes):FileVisitResult = {
        val f = path.toFile
        if(f.getName().endsWith(".jpg") ||  
        	f.getName().endsWith(".JPG") ||
			f.getName().endsWith(".jpeg") ||
			f.getName().endsWith(".JPEG")){
        	list = f.getParentFile() +: list
        	FileVisitResult.SKIP_SIBLINGS
        }else{
          FileVisitResult.CONTINUE
        } 
      }
    }
    Files.walkFileTree(start.toPath(), fileListerner)
    val files = list.map{f => f -> f.toPath()}.map{case (file, path) =>
      Option(new File(start, file.getName())).filterNot{
        _.exists()
      }.map{p => 
        Files.move(path, p.toPath(), StandardCopyOption.ATOMIC_MOVE)
      }.getOrElse(new File(start, file.getName()).toPath())
    }
    /*
    start.listFiles().filterNot(
      _.listFiles().exists{ f =>
      	f.getName().endsWith(".jpg") ||  
        f.getName().endsWith(".JPG") ||
		f.getName().endsWith(".jpeg") ||
		f.getName().endsWith(".JPEG")
      }
    ).foreach{f =>
//    	Files.delete(f.toPath())
      println(f)
    }
    */
  }
}