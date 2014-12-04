package org.mbari.opencv.demos

import org.mbari.opencv.OpenCV
import org.mbari.swing.Swing
import org.opencv.core.{CvType, Mat}
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-10-02T14:44:00
 */
class EMDDemo1 {

  OpenCV

  private val images = Seq("/Phronima1-internal5/image-004.png",
    "/Phronima1-internal5/image-021.png",
    "/Phronima1-internal5/image-022.png",
    "/Phronima1-internal5/image-037.png")
      .map(getClass.getResource(_).getPath)
      .map(Highgui.imread)
      .map(toCIELAB)

  def run(): Unit = {
    println(s"\nRunning ${getClass.getSimpleName}")
    import org.mbari.opencv._
    images.foreach(Swing.namedWindow("", _))

  }

  def toCIELAB(rgb: Mat): Mat = {
    val lab = Mat.zeros(rgb.rows(), rgb.cols(), rgb.`type`())
    Imgproc.cvtColor(rgb, lab, Imgproc.COLOR_BGR2Lab)
    lab
  }

}

object EMDDemo1 {
  def main(args: Array[String]) {
    new EMDDemo1().run()
  }
}
