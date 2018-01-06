val name = "pgb"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.4"
)

lazy val compilerOptions = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:postfixOps"
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4"
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

val baseSettings = Seq(
  libraryDependencies ++= testDependencies.map(_ % "test"),
  scalacOptions in(Compile, console) := compilerOptions,
  compileScalastyle := scalastyle.in(Compile).toTask("").value,
  (compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value,
  sbtPlugin := true
)

lazy val allSettings = baseSettings ++ buildSettings

lazy val pgb = (project in file("."))
  .settings(
    allSettings
  )
