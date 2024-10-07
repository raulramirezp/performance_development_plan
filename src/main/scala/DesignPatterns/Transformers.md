# Transformation Patterns in Functional Programming
In FP, the concept of transformations revolves around how we can convert data from one type to another while
maintaining immutability and leveraging composability.

## Using Chimney for Transformations
Chimney is a Scala library designed to simplify transformations between case classes and other types of immutable data structures. 
It provides a type-safe, declarative API for transforming one type into another, with support for automatic derivation.

Chimney is especially useful when you're dealing with complex domain models that need to be transformed between layers, 
for example, transforming between DTOs (Data Transfer Objects) and domain objects, or from one domain object to another.

[Chimney](https://chimney.readthedocs.io/en/stable/)