
val scala3Version = "3.5.1"
val scalacheckVersion =  "3.2.19"
lazy val root = project.in(file("."))
  .settings(
    name := "performance_development_plan",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.scalatest" %% "scalatest" % scalacheckVersion % "test"
    )
  )
