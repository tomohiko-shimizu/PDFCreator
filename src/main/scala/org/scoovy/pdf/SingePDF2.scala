package org.scoovy.pdf
import java.io.File
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import com.itextpdf.text.Image
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import com.itextpdf.text.Rectangle
object SingePDF2 {
  def main(args:Array[String]):Unit = {
    val file = new File("""/Users/tomo7105/Downloads/somrie""")
    createPDF(file) 
  } 
   
  def createPDF(folder:File) = {
    
    
    val fName = folder.getAbsolutePath() + ".pdf"
    
    val images = getImages(folder).map{image =>
      image -> new Rectangle(image.getWidth(), image.getHeight())
    }
    
    val document:Document = new Document
    val writer = PdfWriter.getInstance(document, new FileOutputStream(fName))
    writer.setStrictImageSequence(true)
    document.open
    images.foreach{case (image, rectangle) =>
      document.setPageSize(rectangle)
      document.add(image)
    }
    document.close
    /*
    getImages(folder).foreach{image => 
      document.add(image)
      document.newPage()
      document.close
    }
    */
  }
  def getImages(folder:File) = {
    folder.listFiles().sorted.filter{f =>
      f.getName().endsWith(".jpg") ||  
	  f.getName().endsWith(".JPG") ||
	  f.getName().endsWith(".jpeg") ||
	  f.getName().endsWith(".JPEG") 
	}.map{f =>
	  val image = Image.getInstance(f.getAbsolutePath())
	  image.setAlignment(Image.MIDDLE)
	  image
	}
  }
}