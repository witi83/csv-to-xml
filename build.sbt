import sbtassembly.Plugin.AssemblyKeys._

assemblySettings

assemblyOption in assembly ~= { _.copy(includeScala = false) }

name := "CSV 2 XML"

version := "1.0"

jarName in assembly := s"${name.value.replace(" ", "-").toLowerCase}-${version.value}.jar"

scalaVersion := "2.11.2"

mainClass := Some("de.witi.csv.Main")

incOptions := incOptions.value.withNameHashing(true)

fork in run := true

javacOptions ++= Seq("-Xlint:unchecked")
