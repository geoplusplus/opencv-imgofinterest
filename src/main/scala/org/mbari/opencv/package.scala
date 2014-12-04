package org.mbari

import java.awt.image.BufferedImage

import org.opencv.core.Mat

package object opencv {

  implicit def matToBufferedImage(mat: Mat): BufferedImage = OpenCV.matToBufferedImage(mat)

  

}