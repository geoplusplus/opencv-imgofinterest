package org.mbari.opencv

import java.awt.image.{DataBufferByte, BufferedImage}
import java.io.{ByteArrayInputStream, File}
import javax.imageio.ImageIO
import org.opencv.core.{CvType, MatOfByte, Mat, Core}
import org.mbari.nativelib.Native
import org.opencv.highgui.Highgui
import org.slf4j.LoggerFactory

/**
 * This object loads the OpenCV native library. Somewhere in the app, before 
 * using opencv, just call:
 * {{{
 *  val cv = OpenCV 
 * }}}
 * The above command will load the native libraries for you
 */
object OpenCV {

  private[this] val log = LoggerFactory.getLogger(getClass)

  val LIBRARY_NAME = Core.NATIVE_LIBRARY_NAME

  try {
    System.loadLibrary(LIBRARY_NAME)
    log.info(LIBRARY_NAME + " was found on the java.library.path and loaded")
  } catch {
    case e: UnsatisfiedLinkError => extractAndLoadNativeLibraries()
  }

  def extractAndLoadNativeLibraries(): Unit = {
    val libraryName = System.mapLibraryName(LIBRARY_NAME)

    if (libraryName != null) {
      val os = System.getProperty("os.name")
      val tempDir = new File(System.getProperty("java.io.tmpdir"))
      val libraryHome = new File(new File(tempDir, "opencv-imgofinterest"), os.substring(0, 3))
      if (!libraryHome.exists()) {
        libraryHome.mkdirs()
      }

      if (!libraryHome.canWrite) {
        throw new RuntimeException("Unable to extract native libary to " + libraryHome +
          ". Verify that you have write access to that directory");
      }

      new Native(LIBRARY_NAME, "native", libraryHome, getClass.getClassLoader)
    }
    else {
      log.error("A native OpenCV library for your platform is not available")
    }
  }

  def matToBufferedImage(image: Mat): BufferedImage = {
    val byteMat = new MatOfByte
    Highgui.imencode(".png", image, byteMat)
    val bytes = byteMat.toArray
    val inputStream = new ByteArrayInputStream(bytes)
    ImageIO.read(inputStream)
  }

  def bufferedImageToMat(image: BufferedImage): Mat = {
    val pixels = image.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData
    val mat = new Mat(image.getHeight, image.getWidth, CvType.CV_8UC3)
    mat.put(0, 0, pixels)
    mat
  }



}