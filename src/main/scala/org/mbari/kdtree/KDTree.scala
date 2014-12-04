package org.mbari.kdtree

/**
 * This K-d tree is used to aggregate points in to clusters using a balanced tree approach.
 *
 * @author Brian Schlining
 * @since 2014-09-26T11:46:00
 * @param root The root node of the KDTree.
 * @param distanceMetric The metric used to calculate the distance between points. It should be the same
 *                       metric used to build the tree initially.
 * @tparam A The type of point contained in the nodes.
 */
class KDTree[A: DimensionalOrdering] private (val root: KDNode[A],
                                              distanceMetric: DistanceMetric[A]) {

  private[this] val dimensionalOrdering = implicitly[DimensionalOrdering[A]]
  private[this] val dim = dimensionalOrdering.dimensions
  private[this] val ordering = (0 until dim).map(dimensionalOrdering.orderingBy)
  private[this] lazy val points = {
    def collectPoints(n: KDNode[A]): Seq[A] = n match {
      case leaf: KDLeaf[A] => leaf.points
      case branch: KDBranch[A] => (collectPoints(branch.left) ++ collectPoints(branch.right)).flatten
    }
    collectPoints(root)
  }

  /**
   * Find all points within some distance of a given point
   * @param pt
   * @param dist
   * @return
   */
  def findPoints(pt: A, dist: Double): Seq[A] = findPoints(pt, dist, root)

  private def findPoints(pt: A, dist: Double, node: KDNode[A]): Seq[A] = node match {
    case leaf: KDLeaf[A] => leaf.points.filter(distanceMetric.distance(pt, _) <= dist)
    case branch: KDBranch[A] => {
      val d = distanceMetric.distanceAlongDimension(pt, branch.point, branch.dimension)
      if (d > dist) Nil
      else Seq(findPoints(pt, dist, branch.left), findPoints(pt, dist, branch.right)).flatten
    }
  }

}


/**
 * Factory object for kdtrees. Use as: {{{
 *
 *   val tree: KDTree[A] = KDTree(points, distanceMetric) // or
 *   val tree: KDTree[A] = KDTree(points, distanceMetric, maxPoints)
 *
 * }}}
 */
object KDTree {

  /**
   * Factory method
   * @param points
   * @param distanceMetric
   * @param maxPoints
   * @tparam A
   * @return
   */
  def apply[A: DimensionalOrdering](points: Seq[A], distanceMetric: DistanceMetric[A], maxPoints: Int = 2): KDTree[A] = {
    val builder = new KDTreeBuilder(points, distanceMetric, maxPoints)
    new KDTree(builder.build(), distanceMetric)
  }

}


protected[kdtree] class KDTreeBuilder[A: DimensionalOrdering](points: Seq[A], distanceMetric: DistanceMetric[A], maxPoints: Int) {

  private[this] val dimensionalOrdering = implicitly[DimensionalOrdering[A]]
  private[this] val dim = dimensionalOrdering.dimensions
  private[this] val ordering = (0 until dim).map(dimensionalOrdering.orderingBy)

  // presort the points along each dimension
  val sortedPoints = (0 until dim).map(d => points.sorted(ordering(d)))

  def build(): KDNode[A] = {
    val d = findSplitDimension(0, 0, sortedPoints.size, includeD = true)
    if (points.size < maxPoints) KDLeaf(points)
    build(d, 0, points.size - 1)
  }

  private def build(d: Int, startOnD: Int, endOnD: Int): KDNode[A] = {
    if (endOnD - startOnD <= maxPoints) KDLeaf(sortedPoints(d).view(startOnD, endOnD))
    else {
      val d = findSplitDimension(d, startOnD, endOnD)
      val mid = startOnD + (endOnD - startOnD) / 2
      KDBranch(sortedPoints(d)(mid), build(d, startOnD, mid - 1), build(d, mid + 1, endOnD), d)
    }
  }

  private def findSplitDimension(d: Int, startOnD: Int, endOnD: Int, includeD: Boolean = false): Int = {
    val ps = sortedPoints(d).view(startOnD, endOnD)
    val spreads = for (i <- 0 until dim) yield {
      if (!includeD && i == d) Double.MinValue
      else {
        distanceMetric.distanceAlongDimension(ps(0), ps(ps.size - 1), d)
      }
    }
    spreads.zipWithIndex.sorted.map(_._2).last // returns index of dimension with largest spread. Excludes d
  }

}



sealed trait KDNode[A] {
  def size: Int
}

case class KDLeaf[A](points: Seq[A]) extends KDNode[A] {
  lazy val size = points.size
}

/**
 *
 * @param point THe point of the branch split
 * @param left The left node of the kdtree
 * @param right The right node of the kdtree
 * @param dimension The dimension that was split. See also [[DimensionalOrdering]]
 * @tparam A The type of points contained
 */
case class KDBranch[A](point: A, left: KDNode[A], right: KDNode[A], dimension: Int) extends KDNode[A] {
  lazy val size = left.size + right.size + 1
}