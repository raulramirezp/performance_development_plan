# Type Erasure in Scala

## What is Type Erasure?

Let’s say you have the following code:
```
case class Thing[T](value: T)

def processThing(thing: Thing[?]) =
  thing match
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case _ => "Thing of something else"

println(processThing(Thing(1)))
println(processThing(Thing("hello")))
```
Here, we have a generic class called Thing that contains a single value of some generic type `T`.
We also have a function called `processThing` that takes a `Thing`.

`processThing` pattern matches on the inner type of Thing, and returns a String depending on the type.

output
```
Thing of int  
Thing of string
```

### Let’s try something else

We’re going to add another case to our pattern match now:
```
def processThingTE(thing: Thing[?]) = {
  thing match {
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case Thing(value: Seq[Int]) => "Thing of Seq[int]"
    case _ => "Thing of something else"
  }
}
```

Again, this seems pretty reasonable. If we can check the type of the instance to match it against an `Int` or a `String`,
we should be able to match it against a `Seq[Int]`.

When we compile this, however, we see the following warning:

```
1 warning found
def processThingTE(thing: Thing[?]): String
-- [E092] Pattern Match Unchecked Warning: -------------------------------------
5 |    case Thing(value: Seq[Int]) => "Thing of Seq[int]"
  |               ^
  |the type test for Seq[Int] cannot be checked at runtime because its type arguments can't be determined from Any
  |
  | longer explanation available when compiling with `-explain`

```

We’ve hit an instance of what’s called “type erasure”. 
What this means is that when Scala is compiled, if there are generic types in the program, 
`information about the specific is checked during compiled time, 
but not available for the runtime to use.`

### What does it mean for types to be erased?

To be more specific about what’s happening here–there is a distinction between how generic types are treated at 
`compile-time` in a Scala program, and how they are treated at `runtime`.

As an example, say you have the following:

```
val seq : Seq[String] = Seq(1,2,3)
```

The **Scala compiler** will check to make sure that value that you are assigning to `seq`, which we are asserting is a `Seq[String]`.
And we will get a type mismatch error in compilation.

```
[E007] Type Mismatch Error:
```

lets say that we have `val seq : Seq[Int] = Seq(1,2,3)`, after these checks happen,
the compiler then removes the specific type information from generics, 
and this code will become the following, as far as the **Java bytecode** that is produced is concerned:

```
val seq : Seq = Seq(1,2,3)
```

**Because of the compile-time checks, we get nice, type-safe code. 
However, the problem with the “underlying” types getting erased is that you can no longer do runtime checks on those types.**

For instance, if you were to try:

```
val seq : Seq[Int] = Seq(1,2,3)
seq.isInstanceOf[Seq[Int]]
```
is obviously true.

and 
```
seq.isInstanceOf[Seq[String]]
```
is obviously false. but this strangely also returns true! 
Again, this is because this code compiles to `seq.isInstanceOf[Seq]`, since the “_underlying_” types are erased.

To elaborate on this a little further, typing `seq.isInstanceOf[Seq[String]]` does not actually violate any type checks,
so it’s valid Scala code. Scala will produce warnings for this, however.

```
1 warning found
-- [E092] Pattern Match Unchecked Warning: -------------------------------------
1 |seq.isInstanceOf[Seq[String]]
  |^^^
  |the type test for Seq[String] cannot be checked at runtime because its type arguments can't be determined from Seq[Int]
  |
  | longer explanation available when compiling with `-explain`
val res2: Boolean = true
```

So if we execute
```
processThingTE(Thing(Seq(1,2,3)))
```
This yields the string “Thing of Seq[Int]”.

And if we run this line:
```
processThingTE(Thing(Seq("hello", "yo")))
```
It also yields the string “Thing of Seq[Int]”.

Annoying, but again, we now know why it happens! The line:

```
case Thing(value: Seq[Int]) => "Thing of Seq[Int]"
```
compiles down to:
```
case Thing(value: Seq) => "Thing of Seq[Int]"
```

This behavior is surprising for the developer, but things can be even worse when in the pattern match, 
you make assumption about what kind of sequence you have. 
For instance, if processThing is implemented as:

```
def processThing(thing: Thing[?]) = {
  thing match
    case Thing(value: Int) => "Thing of int"
    case Thing(value: String) => "Thing of string"
    case Thing(value: Seq[Int]) => "ints sum to " + value.sum
    case _ => "Thing of something else"
}
```
Here, value.sum is only valid if value is a `Seq` of numeric types, like `Int`.
If value is a `Seq[String]`, this will blow up with the error:

```
java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Integer (java.lang.String and java.lang.Integer are in module java.base of loader 'bootstrap')
```

## Can we solve this with reflection?

- Scala 2 provides a **reflection API** that allows you to inspect the types of your instances **at runtime**, 
that can be used to accomplish some of what we want.
 
- It is not available in Scala 3 in any form
### TypeTag - Scala 2 only
The Scala **reflection API** has a mechanism called **TypeTag** that allows you to inspect the type of instances,
including the types of generics, at runtime. 
As the docs, state “_**TypeTags can be thought of as objects which carry along all type information available at compile time, to runtime**_”.

### Using ADT's

```
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
```

___
References:
- https://contributors.scala-lang.org/t/scala-3-and-reflection/3627/6
- https://squidarth.com/scala/types/2019/01/11/type-erasure-scala.html
- 