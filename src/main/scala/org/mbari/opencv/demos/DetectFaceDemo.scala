package org.mbari.opencv.demos

import org.mbari.opencv.OpenCV
import org.opencv.core.{Core, MatOfRect, Point, Scalar}
import org.opencv.highgui.Highgui
import org.opencv.objdetect.CascadeClassifier

class DetectFaceDemo {

  val cv = OpenCV

  def run(): Unit = {
    println(s"\nRunning ${getClass.getSimpleName}")

    // create a face detector
    val faceDetector = new CascadeClassifier(getClass.getResource("/lbpcascade_frontalface.xml").getPath)
    val image = Highgui.imread(getClass.getResource("/lena1.png").getPath)

    // Detect faces in the image.
    // MatOfRect is a special container class for Rect.
    val faceDetections = new MatOfRect
    faceDetector.detectMultiScale(image, faceDetections)

    println(s"Detected ${faceDetections.toArray.length} faces")

    // Draw a bounding box areound each face
    for (rect <- faceDetections.toArray) {
      Core.rectangle(image, new Point(rect.x, rect.y), 
          new Point(rect.x + rect.width, rect.y + rect.height), 
          new Scalar(0, 255, 0))
    }

    val filename = s"${getClass.getSimpleName}.png"
    println(s"Writing $filename")
    Highgui.imwrite(filename, image)

  }
  
}

object DetectFaceDemo {
  def main(args: Array[String]): Unit = {
    new DetectFaceDemo().run()
  }
  
}