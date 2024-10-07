import java.util.UUID
import io.scalaland.chimney.dsl._

final case class Name(value: String) extends AnyVal
final case class Person(name: String, age: Int)
final case class Customer(id: UUID, name: Name, age: Int)

val person = Person(name = "Bob", age = 27)

val customer: Customer =
  person
    .into[Customer]
    .withFieldComputed(_.id, _ => UUID.randomUUID())
    .transform

println(customer)

// More realistic example: transforming DTOs into domain objects

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

println(
  User(
    Username("John"),
    List(Address("Paper St", "Somewhere")),
    RecoveryMethod.Email("john@example.com")
  ).transformInto[UserDTO]
)
