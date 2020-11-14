name := "cs434-project"

version := "0.1"

scalaVersion := "2.13.3"

// sub project settings

lazy val root = project
  .in(file("."))
  .aggregate(
    common,
    master,
    slave
  )

lazy val common = project
  .settings(
    name := "common",
    libraryDependencies ++= commonDependencies,
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value / "scalapb"
    )
  )

lazy val master = project
  .settings(
    name := "master",
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(common)

lazy val slave = project
  .settings(
    name := "slave",
    libraryDependencies ++= commonDependencies
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(common)

lazy val commonDependencies = Seq(
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)