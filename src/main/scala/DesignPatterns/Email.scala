package DesignPatterns

import scala.util.matching.Regex

private val emailPattern: Regex = """^(\w+)@(\w+(.\w+)+)$""".r

sealed abstract case class Email private(address: String)

object Email {
  def fromString(value: String): Option[Email] =
    value match
      case emailPattern(localPart, domainName, _) => Option(new Email(value){})
      case _ =>  None
}
