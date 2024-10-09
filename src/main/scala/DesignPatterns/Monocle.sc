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

val vector: Vector[Int] = listToVector.get(List(1, 2, 3))
val list: List[Int] = vectorToList.get(Vector(1, 2, 3))

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

// DTO classes
case class AddressDTO(street: String, city: String)

case class PhoneDTO(number: String)

case class EmailDTO(email: String)

sealed trait RecoveryMethodDTO

object RecoveryMethodDTO {
  case class Phone(value: PhoneDTO) extends RecoveryMethodDTO

  case class Email(value: EmailDTO) extends RecoveryMethodDTO
}

case class UserDTO(
                    name: String, // 1. primitive
                    addresses: Seq[AddressDTO], // 2. Seq collection
                    recovery: Option[RecoveryMethodDTO] // 3. Option type
                  )

// Domain classes
case class Username(name: String) extends AnyVal

case class Address(street: String, city: String)

//  flat enum
enum RecoveryMethod:
  case Phone(number: String)
  case Email(email: String)

case class User(
                 name: Username, // 1. value class
                 addresses: List[Address], // 2. List collection
                 recovery: RecoveryMethod // 3. non-Option type
               )

// Transforming:
val f1: List[Address] => Seq[AddressDTO] = la => la.map(a => AddressDTO(street = a.street, city = a.city))
val f2: Seq[AddressDTO] => List[Address] = dtol => dtol.map(dto => Address(street = dto.street, city = dto.city)).toList

val addressToDTO = Iso[List[Address], Seq[AddressDTO]](f1)(f2)

val f3: RecoveryMethod => Option[RecoveryMethodDTO] = {
  case RecoveryMethod.Phone(number) => Some(RecoveryMethodDTO.Phone(PhoneDTO(number = number)))
  case RecoveryMethod.Email(email) => Some(RecoveryMethodDTO.Email(EmailDTO(email = email)))
}

val f4: Option[RecoveryMethodDTO] => RecoveryMethod = {
  case Some(RecoveryMethodDTO.Phone(PhoneDTO(number))) => RecoveryMethod.Phone(number)
  case Some(RecoveryMethodDTO.Email(EmailDTO(email))) => RecoveryMethod.Email(email)
  case None => RecoveryMethod.Phone("11111")
}

val recoveryMethodToDTO = Iso[RecoveryMethod, Option[RecoveryMethodDTO]](f3)(f4)

val f5: Username => String = username => username.name
val f6: String => Username = username => Username(username)

val usernameToString = Iso[Username, String](f5)(f6)

val f7: User => UserDTO = u => UserDTO(name = usernameToString.get(u.name), addresses = addressToDTO.get(u.addresses), recovery = recoveryMethodToDTO.get(u.recovery))
val f8: UserDTO => User = dto => User(name = usernameToString.reverseGet(dto.name), addresses = addressToDTO.reverseGet(dto.addresses), recovery = recoveryMethodToDTO.reverseGet(dto.recovery))

val userToDTO = Iso[User, UserDTO](f7)(f8)

val userDTO: UserDTO = userToDTO.get(
  User(
    Username("John"),
    List(Address("Paper St", "Somewhere")),
    RecoveryMethod.Email("john@example.com")
  )
)

val user: User = userToDTO.reverseGet(userDTO)
println(userDTO)
println(user)

