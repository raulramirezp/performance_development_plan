import DesignPatterns.Email

import scala.util.matching.Regex

Email.fromString("JohnSmith@sample.domain.com")
Email.fromString("Invalid eman @")

// The following lines don't compile
//val email = new Email("test") {}
//val email2 = new Email(){}
