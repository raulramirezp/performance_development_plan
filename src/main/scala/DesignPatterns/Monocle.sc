/**
 * ISO
 */

import monocle.Iso
import monocle.macros.GenIso

case class Person(name: String, age: Int)

val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)) { case (name, age) => Person(name, age) }

val person: Person = personToTuple(("Pedro", 30))

val tuple: (String, Int) = personToTuple.get(Person("Luisa", 25))

/**
 * Another common use of Iso is between collection. List and Vector represent the same concept, they are both an ordered sequence of elements
 * but they have different performance characteristics.
 * Therefore, we can define an Iso between a List[A] and a Vector[A]:
 */

def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)
def vectorToList[A] = listToVector[A].reverse

val vector: Vector[Int] = listToVector.get(List(1,2,3))
val list: List[Int] = vectorToList.get(Vector(1,2,3))

/**
 * Iso are also convenient to lift methods from one type to another, for example a String can be seen as a List[Char]
 * so we should be able to transform all functions List[Char] => List[Char] into String => String:
 */

val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))

val charList: List[Char] = stringToList.get("Hello")
val string = stringToList.modify(_.tail)("Hello")

/**
 * Iso Generation
 */

GenIso.fields[Person].get(Person("John", 42))