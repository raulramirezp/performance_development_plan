import RecoveryMethod.{Email, Phone}
import monocle.Lens
import monocle.macros.GenLens

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
                 recovery: RecoveryMethod, // 3. non-Option type
                 age: Int
               )

//val username: Lens[User, String] = Lens[User, String](_.name.name)(newName => user => user.copy(name = Username(newName)))
val username: Lens[User, String] = GenLens[User](_.name.name)

val user1 = User(Username("Lina"), List(Address("Carrera 1 # 1", "Bogotá")), RecoveryMethod.Email("lina@correo.com"), 28)

username.get(user1)
username.replace("Marcela")(user1)
// modify is equivalent to call get and then replace
username.modify(_ + " Marcela")(user1)

def users(u: String): List[String] =
  List(0,1,2,3,4).map(n => s"user$n")

username.modifyF(users)(user1)

// Composition v
GenLens[User](_.name.name).replace("Mike") compose GenLens[User](_.age).modify(_ + 1)