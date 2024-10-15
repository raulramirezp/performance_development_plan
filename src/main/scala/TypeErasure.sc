case class Thing[T](value: T)

/*
The syntax of wildcard arguments in types is changing from _ to ?.
We pick ? as a replacement syntax for wildcard types, since it aligns with Java's syntax.
 */
def processThing(thing: Thing[?]) =
  thing match
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case _ => "Thing of something else"

println(processThing(Thing(1)))
println(processThing(Thing("hello")))

def processThingTE(thing: Thing[?]) = {
  thing match {
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case Thing(value: Seq[Int]) => "Thing of Seq[int]"
    case _ => "Thing of something else"
  }
}

println(processThingTE(Thing(1)))
println(processThingTE(Thing("hello")))

/**
 * We’ve hit an instance of what’s called “type erasure”.
 * What this means is that when Scala is compiled, if there are generic types in the program,
 * information about the specific is checked during compiled time, but not available for the runtime to use.
 */
processThingTE(Thing(Seq(1,2,3)))
processThingTE(Thing(Seq("hello", "yo")))

val seq : Seq[Int] = Seq(1,2,3)
seq.isInstanceOf[Seq[Int]]
seq.isInstanceOf[Seq[String]]

def unsafeProcessThing(thing: Thing[?]) = {
  thing match
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case Thing(value: Seq[Int]) => "ints sum to " + value.sum
    case _ => "Thing of something else"
}

unsafeProcessThing(Thing(Seq("hello", "yo")))

// Using ADT's to addressing type erasure errors

sealed trait ThingValue
case class SeqIntThingValue(value: Seq[Int]) extends ThingValue
case class SeqStringThingValue(value: Seq[String]) extends ThingValue

def processThingValue[T <: ThingValue](thing: Thing[T]) = {
  thing match {
    case Thing(SeqIntThingValue(value: Seq[Int])) => "Seq of Int: " + value.sum
    case Thing(SeqStringThingValue(value: Seq[String])) => "Seq of String: " + value.foldLeft("")(_ + _)
    case _ => "Other thing"
  }
}

processThingValue(Thing(SeqIntThingValue(Seq(1,2,3))))
processThingValue(Thing(SeqStringThingValue(Seq("hello", "yo"))))