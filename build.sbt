name := "cs434-project"

version := "0.1"

scalaVersion := "2.13.3"

// sub project settings

lazy val root = project
  .in(file("."))
  .disablePlugins(AssemblyPlugin)
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
  .disablePlugins(AssemblyPlugin)

lazy val master = project
  .settings(
    name := "master",
    assemblySettings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(common)

lazy val slave = project
  .settings(
    name := "slave",
    assemblySettings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(common)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val commonDependencies = Seq(
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)