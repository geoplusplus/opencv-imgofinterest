package org.mbari.opencv.demos

import java.util.{ArrayList, List => JList}
import org.mbari.opencv.OpenCV
import org.mbari.swing.Swing
import org.opencv.core.{Core, Size, CvType, Mat}
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc
import scala.collection.JavaConverters._
import scala.util.Try


/**
 * From https://stackoverflow.com/questions/11456565/opencv-mean-sd-filter 3
 * Run in SBT using:
 * {{{run-main org.mbari.opencv.demos.StdByRegionDemo /Users/brian/Desktop/V8Zso.jpg}}}
 */
object  StdByRegionDemo {
  
  def main(args: Array[String]): Unit = {
    import org.mbari.opencv.matToBufferedImage

    OpenCV // initialize opencv
    val src = Highgui.imread(args(0)) // OpenCV used BGR instead of RGB!!
    
    val n: Double = if (args.size > 1) Try(args(1).toDouble).getOrElse(3D)
        else 3D

    // CvType.CV_32F works the same as CvType.CV_32FC3. I tried both
    val image32f = Mat.zeros(src.rows(), src.cols(), CvType.CV_32F)
    src.convertTo(image32f, CvType.CV_32F)

    val mu = Mat.zeros(src.rows(), src.cols(), CvType.CV_32F)
    Imgproc.blur(image32f, mu, new Size(n, n))

    val mu2 = Mat.zeros(src.rows(), src.cols(), CvType.CV_32F)
    Imgproc.blur(image32f.mul(image32f), mu2, new Size(n, n))

    val sigma = Mat.zeros(src.rows(), src.cols(), CvType.CV_32F)
    val temp = Mat.zeros(src.rows(), src.cols(), CvType.CV_32F)
    Core.subtract(mu2, mu.mul(mu), temp)
    Core.sqrt(temp, sigma)

    val norm = normalize(sigma)

    val gray = Mat.zeros(src.rows(), src.cols(), CvType.CV_8U)
    Imgproc.cvtColor(norm, gray, Imgproc.COLOR_RGB2GRAY)

    val channels = new ArrayList(List(Mat.zeros(src.rows(), src.cols(), CvType.CV_8U),
        Mat.zeros(src.rows(), src.cols(), CvType.CV_8U),
        Mat.zeros(src.rows(), src.cols(), CvType.CV_8U)).asJava)
    Core.split(norm, channels)

    Swing.namedWindow("Original", src)
    Swing.namedWindow("Mean", mu)
    Swing.namedWindow("Std", sigma)
    Swing.namedWindow("Normalized", norm)
    Swing.namedWindow("Normalized Grayscale", gray)
    Swing.namedWindow("Normalized Red Channel", channels.get(2))

  }

  def normalize(src: Mat): Mat = {
    val dst = Mat.zeros(src.rows(), src.cols(), CvType.CV_8UC1)
    Core.normalize(src, dst, 0.0, 255.0, Core.NORM_MINMAX)
    dst
  }
}

/* From C-source

#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;

Mat mat2gray(const Mat& src)
{
    Mat dst;
    normalize(src, dst, 0.0, 1.0, NORM_MINMAX);
    return dst;
}

int main()
{
    Mat image = imread("coke-can.jpg", 0);

    Mat image32f;
    image.convertTo(image32f, CV_32F);

    Mat mu;
    blur(image32f, mu, Size(3, 3));

    Mat mu2;
    blur(image32f.mul(image32f), mu2, Size(3, 3));

    Mat sigma;
    cv::sqrt(mu2 - mu.mul(mu), sigma);

    imshow("coke", mat2gray(image32f));
    imshow("mu", mat2gray(mu));
    imshow("sigma",mat2gray(sigma));
    waitKey();
    return 0;
}

 */
