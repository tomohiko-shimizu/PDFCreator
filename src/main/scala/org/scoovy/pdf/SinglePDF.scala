package org.scoovy.pdf
import java.io.File

object SinglePDF {
  def main(args:Array[String]):Unit = {
    val file = new File("""/Users/tomo7105/Downloads/neuro/ネウロ06""")
    PDFCreator.createPDF(file)
  }
}