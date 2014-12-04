package org.mbari.kdtree

import scala.annotation.tailrec

/**
 * Strategy for ordering a multi-dimensional type along a given dimension
 *
 * @author Brian Schlining
 * @since 2014-09-26T10:57:00
 */
trait DimensionalOrdering[A] {

  /**
   * @return The number of dimensions
   */
  def dimensions: Int

  /**
   * Compare some type along a given dimension
   *
   * @param dimension The dimension to compare
   * @param x 1st value to compare
   * @param y 2nd value to compare
   * @return negative if x' < y', positive if x' > y' and zero if x' == y'
   */
  def compareByDimension(x: A, y: A, dimension: Int): Int

  /**
   * Returns an Ordering of A in which the given dimension is the primary ordering criteria.
   * If x and y have the same projection on that dimension, then they are compared on the lowest
   * dimension that is different.
   *
   * @param dimension The primary dimension to compare.
   * @return An ordering for type A along a given dimension
   */
  def orderingBy(dimension: Int): Ordering[A] = new Ordering[A] {
    override def compare(x: A, y: A): Int = {
      compareByDimension(x, y, dimension) match {
        case c if c != 0 => c               // Compared dimension and they were different
        case 0 => compareDimension(x, y, 0) // Compared dimension and the they were the same, use other dimensions for comparison
      }
    }

    @tailrec
    private def compareDimension(x: A, y: A, dimension: Int): Int = {
      if (dimension == dimensions) 0
      else {
        val c = compareByDimension(x, y, dimension)
        if (c != 0) c else compareDimension(x, y, dimension + 1)
      }
    }
  }

}


object DimensionalOrdering {
  def dimensionalOrderingForTuple[T <: Product,A](dim: Int)(implicit ord: Ordering[A]) =
    new DimensionalOrdering[T] {
      val dimensions = dim
      def compareByDimension(x: T, y: T, d: Int) = ord.compare(
        x.productElement(d).asInstanceOf[A], y.productElement(d).asInstanceOf[A])
    }

  implicit def dimensionalOrderingForTuple2[A](implicit ord: Ordering[A]) =
    dimensionalOrderingForTuple[(A, A), A](2)

  implicit def dimensionalOrderingForTuple3[A](implicit ord: Ordering[A]) =
    dimensionalOrderingForTuple[(A, A, A), A](3)

  implicit def dimensionalOrderingForTuple4[A](implicit ord: Ordering[A]) =
    dimensionalOrderingForTuple[(A, A, A, A), A](4)

  implicit def dimensionalOrderingForTuple5[A](implicit ord: Ordering[A]) =
    dimensionalOrderingForTuple[(A, A, A, A, A), A](5)
}