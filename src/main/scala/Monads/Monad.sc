import scala.util.Try

trait Functor[F[_]]:
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B]

trait Monad[F[_]] extends Functor[F]:
  def unit[A](a: => A): F[A]
  
  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C]
  
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]

    def map[B](f: A => B): F[B] =
      flatMap(a => unit(f(a)))

    def map2[B, C](fb: F[B])(f: (A, B) => C): F[C] =
      fa.flatMap(a => fb.map(b => f(a, b)))


sealed trait Maybe[+A]

final case class Full[A](value: A) extends Maybe[A]

case object Empty extends Maybe[Nothing]


given maybeMonad: Monad[Maybe] with
  override def unit[A](a: => A): Maybe[A] = Full(a)

  def compose[A, B, C](f: A => Maybe[B], g: B => Maybe[C]): A => Maybe[C] =
    a => f(a).flatMap(g)
    
  extension [A](as: Maybe[A])
    def flatMap[B](f: A => Maybe[B]): Maybe[B] =
      as match
        case Full(value) => f(value)
        case Empty => Empty


val intToString: Int => String = number => s"The number ${number.toString}"
val intToMaybeString: Int => Maybe[String]= number => if (number > 0) Full(number.toString) else Empty
val stringToLong: String => Maybe[Long] = value => Try(value.toLong).fold(_ => Empty, Full(_))

val possible: Maybe[Int] = Full(10)
val transformed: Maybe[String] = possible.flatMap(intToMaybeString)
println(possible.map(intToString))
println(transformed)
println(Full(-1).flatMap(intToMaybeString))

// Checking monad laws
//Left identity
assert(maybeMonad.unit(10).flatMap(intToMaybeString) == intToMaybeString(10))
// Right identity
assert(possible.flatMap(maybeMonad.unit(_)) == possible)
// Associativity
assert(possible.flatMap(intToMaybeString).flatMap(stringToLong) == possible.flatMap(x => intToMaybeString(x).flatMap(stringToLong)))

// Checking monad laws with Kleisli
def kleisliCompose[F[_], A, B, C](f: A => F[B], g: B => F[C])(m: Monad[F]): A => F[C] = {
  (a: A) => m.flatMap(f(a))(g)
}

val f = intToMaybeString
val g = stringToLong
val h: Long =>  Maybe[String] = number => Full(s"The number ${number.toString}")

kleisliCompose[Maybe, String, Long, String](g, h)(maybeMonad)
val left = kleisliCompose(f, kleisliCompose(g, h)(maybeMonad))(maybeMonad)
val right = kleisliCompose(kleisliCompose(f, g)(maybeMonad), h)(maybeMonad)

possible.flatMap(left)
possible.flatMap(right)
possible.flatMap(left) == possible.flatMap(right)

Full(55).flatMap(left)
Full(100).flatMap(right)

Full(70).flatMap(maybeMonad.compose(maybeMonad.compose(f, g), h))
Full(70).flatMap(maybeMonad.compose(f, maybeMonad.compose(g, h)))