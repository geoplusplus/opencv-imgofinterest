package org.mbari.opencv.demos

import org.mbari.opencv.OpenCV
import org.mbari.swing.Swing
import org.opencv.core.Mat
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-10-02T15:12:00
 */
object DisplayDemo1 {

  def main(args: Array[String]) {

    OpenCV // intialize opencv

    // Read original image
    val src = Highgui.imread(getClass.getResource("/lena1.png").getPath)

    // Convert color image to gray level image
    val image = Mat.zeros(src.rows(), src.cols(), src.`type`())
    Imgproc.cvtColor(src, image, Imgproc.COLOR_RGB2GRAY)
    Swing.namedWindow("Image", OpenCV.matToBufferedImage(image))

  }

}
