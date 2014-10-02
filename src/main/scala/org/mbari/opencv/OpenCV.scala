package org.mbari.opencv

import java.io.File
import org.opencv.core.Core
import org.mbari.nativelib.Native
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
        libraryHome.mkdirs();
      }

      if (!libraryHome.canWrite()) {
        throw new RuntimeException("Unable to extract native libary to " + libraryHome +
          ". Verify that you have write access to that directory");
      }

      new Native(LIBRARY_NAME, "native", libraryHome, getClass.getClassLoader)
    }
    else {
      log.error("A native OpenCV library for your platform is not available")
    }
  }
}