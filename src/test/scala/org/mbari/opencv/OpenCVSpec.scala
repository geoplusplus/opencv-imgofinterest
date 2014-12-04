package org.mbari.opencv

import java.io.File
import java.net.URL
import javax.imageio.ImageIO

import org.opencv.highgui.Highgui
import org.scalatest.{Matchers, FlatSpec}

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-10-02T17:20:00
 */
class OpenCVSpec extends FlatSpec with Matchers {

  val imgPath = getClass.getResource("/lena1.png")

  "OpenCV" should " initialize correctly" in {
    OpenCV
  }


  it should "convert a Mat to a BufferedImage" in {
    val mat = Highgui.imread(imgPath.getPath)
    mat should not be null
    val bufferedImage = OpenCV.matToBufferedImage(mat)
    bufferedImage should not be null
    ImageIO.write(bufferedImage, "png", new File("target/lena1-matToBufferedImage.png"))
  }

  it should "convert a BufferedImage to a Mat" in {
    val image = ImageIO.read(imgPath)
    image should not be null
    val mat = OpenCV.bufferedImageToMat(image)
    mat should not be null
    Highgui.imwrite("target/lena1-bufferedImageToMat.png", mat)

  }



}
