package org.mbari.kdtree

import scala.math._
import scala.math.Numeric.Implicits._

/**
 *
 *
 * @author Brian Schlining
 * @since 2014-09-26T11:15:00
 */
trait DistanceMetric[A] {

  /**
   *
   * @param x
   * @param y
   * @return Returns a distance between x and y
   */
  def distance(x: A, y: A): Double

  /**
   * Returns the distance between x and the plane that passes through y that's perpendicular to the provided dimension
   *
   * @param x
   * @param y
   * @param dimension The dimension that we're calculating the distance along
   */
  def distanceAlongDimension(x: A, y: A, dimension: Int): Double

}

object DistanceMetric {
  implicit def distanceFromTuple2[A: Numeric] = new DistanceMetric[(A, A)] {

    override def distance(x: (A, A), y: (A, A)): Double = {
      val dx = x._1 - y._1
      val dy = x._2 - y._2
      sqrt((dx * dx + dy * dy).toDouble())
    }

    override def distanceAlongDimension(x: (A, A), y: (A, A), dimension: Int): Double = {
      (x.productElement(dimension).asInstanceOf[A] - y.productElement(dimension).asInstanceOf[A]).toDouble()
    }
  }
}
