import monocle.Prism

sealed trait Json
case object JNull extends Json
case class JStr(v: String) extends Json
case class JNum(v: Double) extends Json
case class JObj(v: Map[String, Json]) extends Json

val jStr = Prism[Json, String]{
  case JStr(v) => Some(v)
  case _       => None
}(JStr.apply)

jStr("hello")
jStr.getOption(JStr("Hello"))
jStr.getOption(JNum(3.2))

// Another shorted way to define the prism using a partial function
val jStr2 = Prism.partial[Json, String]{case JStr(v) => v}(JStr.apply)

jStr2("hello")
jStr2.getOption(JStr("Hello"))
jStr2.getOption(JNum(3.2))

// We can also modify and replace
jStr.replace("Bar")(JStr("Hello"))
jStr.modify(_.reverse)(JStr("Hello"))

//If we care about the success or failure of the update, we can use replaceOption or modifyOption:
jStr.modifyOption(_.reverse)(JStr("Hello"))
jStr.modifyOption(_.reverse)(JNum(10))
