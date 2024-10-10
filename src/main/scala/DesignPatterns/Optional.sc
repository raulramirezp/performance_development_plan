import monocle.Optional

val head = Optional[List[Int], Int] {
  case Nil => None
  case x :: xs => Some(x)
} { a => {
  case Nil => Nil
  case x :: xs => a :: xs
  }
}

val xs = List(1, 2, 3)
val ys = List.empty[Int]

val isXSNotEmpty: Boolean = head.nonEmpty(xs)
val isYSNotEmpty: Boolean =  head.nonEmpty(ys)

// We can replace and modify if head is present
head.getOption(xs)
head.replace(5)(xs)
head.modify(_ + 1)(xs)

// When the list is empty, replace and modify returns an empty list
head.getOption(ys)
head.replace(5)(ys)
head.modify(_ + 1)(ys)
