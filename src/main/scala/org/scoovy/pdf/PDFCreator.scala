package org.scoovy.pdf
import java.io.File
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.PageSize
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.io.IOException
import java.nio.file.StandardCopyOption
object PDFCreator {
  
  val removeVisitor = new SimpleFileVisitor[Path]{
    override def visitFile(path:Path, attrs:BasicFileAttributes) = {
      if(!path.toFile().getName().endsWith(".pdf")){
        Files.delete(path)
      }
      FileVisitResult.CONTINUE
    }
    override def postVisitDirectory(dir:Path, exc:IOException) = {
      Files.delete(dir)
      FileVisitResult.CONTINUE
    }
  }
  def rename(files:Seq[File]) = {
    files.foreach{f =>
      val name = f.getName()
      val newName = Setting.name + name.replaceAll("""\D""", "") + ".pdf" 
      val newFile = new File(f.getParentFile(), newName)
      f.renameTo(newFile)
    }
  }
  def main(args:Array[String]):Unit = {
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
    var folder = new File("""/Users/tomo7105/Documents/comics/""" + Setting.name)
    Files.walkFileTree(folder.toPath(), fileListerner)
    val files = list.map{f => f -> f.toPath()}.map{case (file, path) =>
      Option(new File(folder, file.getName())).filterNot{
        _.exists()
      }.map{p => 
        Files.move(path, p.toPath(), StandardCopyOption.ATOMIC_MOVE)
      }.getOrElse(new File(folder, file.getName()).toPath())
    }
    folder = new File("""/Users/tomo7105/Documents/comics/""" + Setting.name)
    val listFiles = folder.listFiles
    
    listFiles.filter{containsJpeg}.zipWithIndex.foreach{case (f, number) =>
      println("document" + (number + 1) + " start")
	  createPDF(f)
	}
    listFiles.filterNot(_.getName().endsWith(".pdf")).foreach{f =>
      Files.walkFileTree(f.toPath(), removeVisitor)
      println(f)
    }
    folder = new File("""/Users/tomo7105/Documents/comics/""" + Setting.name)
    rename(folder.listFiles.filter(_.getName().endsWith(".pdf")))
  }
  def containsJpeg(file:File) = {
    file.isDirectory() &&
    file.listFiles().exists(f =>
      f.getName().endsWith(".jpg") ||  
	  f.getName().endsWith(".JPG") ||
	  f.getName().endsWith(".jpeg") ||
	  f.getName().endsWith(".JPEG") 
    )
  }
  def createPDF(folder:File) = {
    val document = new Document(PageSize.A4, 0, 0, 0, 0)
    val fName = folder.getAbsolutePath() + ".pdf"
    PdfWriter.getInstance(document, new FileOutputStream(fName))
    document.open
    getImages(folder, document).foreach{image => 
      document.add(image)
					document.newPage()
				      }
    document.close
  }
  def getImages(folder:File, document:Document) = {
    val documentsize = document.getPageSize()
    val documentWidth = documentsize.getWidth()
    val documentHeight = documentsize.getHeight()
    folder.listFiles().sorted.filter{f =>
      f.getName().endsWith(".jpg") ||  
	  f.getName().endsWith(".JPG") ||
	  f.getName().endsWith(".jpeg") ||
	  f.getName().endsWith(".JPEG") 
	}.map{f =>
	  val image = Image.getInstance(f.getAbsolutePath())
	  val widthScale = documentWidth/image.getWidth() * 100
	  val heightScale = documentHeight/image.getHeight() * 100
	  val scale = List(widthScale, heightScale).min
	  image.setAlignment(Image.MIDDLE)
	  image.scalePercent(scale)
	  image
	}
  }
}
