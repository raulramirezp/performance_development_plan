import monocle.Traversal
import cats.implicits._   // to get all cats instances including Traverse[List]

val xs = List(1,2,3,4,5)

val eachL = Traversal.fromTraverse[List, Int]
// eachL: Traversal[List[Int], Int] = monocle.PTraversal$$anon$2@2aa3c162
eachL.replace(0)(xs)
// res0: List[Int] = List(0, 0, 0, 0, 0)
eachL.modify(_ + 1)(xs)
// res1: List[Int] = List(2, 3, 4, 5, 6)

//  few interesting methods to query our data:
eachL.getAll(xs)
// res2: List[Int] = List(1, 2, 3, 4, 5)
eachL.headOption(xs)
// res3: Option[Int] = Some(value = 1)
eachL.find(_ > 3)(xs)
// res4: Option[Int] = Some(value = 4)
eachL.all(_ % 2 == 0)(xs)
// res5: Boolean = false

// Traversal also offers smart constructors to build a Traversal for a fixed number of target (currently 2 to 6 targets):
case class Point(id: String, x: Int, y: Int)

val points = Traversal.apply2[Point, Int](_.x, _.y)((x, y, p) => p.copy(x = x, y = y))
points.replace(5)(Point("bottom-left",0,0))