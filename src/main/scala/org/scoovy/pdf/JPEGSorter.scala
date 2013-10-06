package org.scoovy.pdf
import java.io.File
import java.nio.file.Files

object JPEGSorter {
	def main(args:Array[String]):Unit = {
	  val file = new File("/Users/tomo7105/Downloads/kimi/Kimi ni todoke 04")
	  val fmaps = file.listFiles().flatMap{f =>
	    val name = f.getName()
	    val r = """\((\d+)\)\.""".r
	    r.findFirstMatchIn(name).filter{_.groupCount >= 1}.map(f -> _.group(1))
	  }
	  fmaps.map{case (f, number) =>
	    f -> ("%03d".format(number.toInt))
	  }.foreach{case (f, number) =>
	    val result = new File(f.getParent(), number + ".jpg")
	    Files.move(f.toPath(), result.toPath())
	  }
	}
}