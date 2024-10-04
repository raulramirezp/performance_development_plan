trait Functor[F[_]]:
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B]


sealed trait Maybe[+A]

final case class Full[A](value: A) extends Maybe[A]

case object Empty extends Maybe[Nothing]

given maybeFunctor: Functor[Maybe] with
  extension [A](as: Maybe[A])
    def map[B](f: A => B): Maybe[B] =
      as match
        case Full(value) => Full(f(value))
        case Empty => Empty


val intToString: Int => String = number => s"The number ${number.toString}"
val stringToUpper: String => String = values => values.toUpperCase

val possible: Maybe[Int] = Full(10)
val transformed: Maybe[String] = possible.map(intToString)
println(transformed)

val empty = Empty

println(empty.map(intToString))

// Functor laws
// identity
assert(possible.map(identity).equals(possible))

// Composition
assert(possible.map(intToString).map(stringToUpper).equals(possible.map(value => stringToUpper(intToString(value)))))