### Functors: Generalizing the map function

## Definition of a Functor Formally:
```
a functor is a type F[A] with an operation map with type (A => B) => F[B].
The general type chart is shown in Figure 3.4.
```
![img.png](functor.png)

## Functor Laws
```
1. Identity: calling map with the identity function is the same as doing nothing:
 fa.map(a => a) == fa 
```
```
2. Composition: mapping with two functions f and g is the same as mapping with f and then mapping with g: 
  fa.map(g(f(_))) == fa.map(f).map(g)
```

#### Higher Kinds and Type Constructors Kinds are like types for types.
They describe the number of “holes” in a type.
We distinguish between regular types that have no holes and “type constructors” that have holes we can fill 
to produce types.

## Defining a Functor
```
trait Functor[F[_]]:
    extension [A](fa: F[A])
        def map[B](f: A => B): F[B]
```
usage 
```
given listFunctor: Functor[List] with
extension [A](as: List[A])
def map[B](f: A => B): List[B] =
as.map(f)
```

# Monads: Generalizing the flatMap and unit functions
