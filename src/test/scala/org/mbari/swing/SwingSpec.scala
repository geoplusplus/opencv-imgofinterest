package org.mbari.swing

import javax.imageio.ImageIO

import org.scalatest.{Matchers, FlatSpec}

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-10-06T16:49:00
 */
class SwingSpec extends FlatSpec with Matchers {

  val imgPath = getClass.getResource("/lena1.png")

  "Swing" should "display a window" in {
    val image = ImageIO.read(imgPath)
    Swing.namedWindow("window 1", image)
  }
}