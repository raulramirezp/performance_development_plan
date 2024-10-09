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

### Iso
An Iso is an optic which converts elements of type S into elements of type A without loss.

#### Iso Laws
An Iso must satisfy all properties defined in IsoLaws from the core module.
You can check the validity of your own Iso using IsoTests from the law module.

In particular, an Iso must verify that get and reverseGet are inverse. 
This is done via roundTripOneWay and roundTripOtherWay laws:

taken from [monocle-iso](https://www.optics.dev/Monocle/docs/optics/iso)