# Functional Programming design patterns

## Transformation
In FP, the concept of transformations revolves around how we can convert data from one type to another while
maintaining immutability and leveraging composability.

### Using Chimney for Transformations
Chimney is a Scala library designed to simplify transformations between case classes and other types of immutable data structures. 
It provides a type-safe, declarative API for transforming one type into another, with support for automatic derivation.

Chimney is especially useful when you're dealing with complex domain models that need to be transformed between layers, 
for example, transforming between DTOs (Data Transfer Objects) and domain objects, or from one domain object to another.

[Chimney](https://chimney.readthedocs.io/en/stable/)

## Optics

Optics provides a way for data access and manipulation in a functional way
because their focus on composability, whenever you apply an operation to a value,
a copy is returned, in contrast with mutable approaches. 

### [Monocle](https://www.optics.dev/Monocle/)
```
    Monocle is a Scala library which offers a simple yet powerful API to access and transform immutable data.
```
___
### Iso
An Iso is an optic which converts elements of type `S` into elements of type `A` without loss.

#### Iso Laws
An Iso must satisfy all properties defined in IsoLaws from the core module.
You can check the validity of your own Iso using IsoTests from the law module.

In particular, an Iso must verify that get and reverseGet are inverse. 
This is done via roundTripOneWay and roundTripOtherWay laws:

___
### Lens

A `Lens` is an optic used to zoom inside a Product, e.g. `case class`, `Tuple`, `HList or even `Map`.

`Lenses` have two type parameters generally called `S` and `A`: `Lens[S, A]` where `S` represents the Product and `A` an element inside of `S.

#### Laws

In particular, a Lens must respect the `getReplace` law which states that if you get a value `A` from `S` and replace it back in,
the result is an object identical to the original one.

A side effect of this law is that replace must only update the `A` it points to, for example it cannot increment a counter or modify another value.

___
### Prism

A `Prism` is an optic used to select part of a `Sum` type (also known as Coproduct), e.g. `sealed trait` or `Enum.

`Prisms` have two type parameters generally called `S` and `A`: `Prism[S, A]` where `S` represents the `Sum` and `A` a part of the `Sum`.

#### Laws

In particular, a Prism must verify that getOption and reverseGet allow a full round trip if the Prism matches i.e. if getOption returns a Some.

```
def partialRoundTripOneWay[S, A](p: Prism[S, A], s: S): Boolean =
  p.getOption(s) match {
    case None    => true // nothing to prove
    case Some(a) => p.reverseGet(a) == s
  }
  
def partialRoundTripOtherWay[S, A](p: Prism[S, A], a: A): Boolean =
  p.getOption(p.reverseGet(a)) == Some(a)
```
___
### Optional

An `Optional` is an `Optic` used to zoom inside a `Product`, e.g. `case class`, `Tuple`, `HList` or even `Map`. 
Unlike the `Lens`, the element that the `Optional` focuses on may not exist.

Optionals have two type parameters generally called `S` and `A`: `Optional[S, A]` where `S` represents the Product and `A` an optional element inside of `S`.

#### Laws

An `Optional` must satisfy all properties defined in `OptionalLaws` in core module. You can check the validity of your own `Optional` using `OptionalTests` in law module.

`getOptionSet` states that if you getOrModify a value `A` from `S` and then replace it back in, the result is an object identical to the original one.

`setGetOption` states that if you replace a value, you always `getOption` the same value back.

```
class OptionalLaws[S, A](optional: Optional[S, A]) {

  def getOptionSet(s: S): Boolean =
    optional.getOrModify(s).fold(identity, optional.replace(_)(s)) == s

  def setGetOption(s: S, a: A): Boolean =
    optional.getOption(optional.replace(a)(s)) == optional.getOption(s).map(_ => a)

}
```
___
### Traversal
A `Traversal` is the generalisation of an `Optional` to several targets. 
In other word, a `Traversal` allows to focus from a type `S` into `0` to `n` values of type `A`.

#### Laws
- A `Traversal` must respect the `modifyGetAll` law which checks that you can modify all elements targeted by a `Traversal`
    ```
    def modifyGetAll[S, A](t: Traversal[S, A], s: S, f: A => A): Boolean =
        t.getAll(t.modify(f)(s)) == t.getAll(s).map(f)
    ```
- Another important law is `composeModify` also known as fusion law:
  ```
  def composeModify[S, A](t: Traversal[S, A], s: S, f: A => A, g: A => A): Boolean =
      t.modify(g)(t.modify(f)(s)) == t.modify(g compose f)(s)
  ```

___

taken from [monocle-docs](https://www.optics.dev/Monocle/docs/optics)
