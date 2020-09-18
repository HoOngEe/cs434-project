name := "cs434-project"

version := "0.1"

scalaVersion := "2.13.3"

// sub project settings

lazy val root = project
  .in(file("."))
  .aggregate(
    master,
    slave
  )
  .dependsOn(
    master,
    slave
  )

lazy val master = project

lazy val slave = project