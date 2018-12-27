import sbt.Keys.libraryDependencies
ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true
ThisBuild / version := "0.1"

lazy val projectName = "zeklin"

// ### Dependencies ###

lazy val squants = "org.typelevel"  %% "squants"        % "1.4.0"
lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

lazy val circe = ((version: String) =>
  Seq(
    "io.circe" %% "circe-core"    % version,
    "io.circe" %% "circe-generic" % version,
    "io.circe" %% "circe-parser"  % version
  ))("0.11.0")

lazy val http4s = ((version: String) =>
  Seq(
    "org.http4s" %% "http4s-blaze-server" % version,
    "org.http4s" %% "http4s-circe"        % version,
    "org.http4s" %% "http4s-dsl"          % version,
  ))("0.20.0-M4")

lazy val testKitLibs = Seq(
  "org.scalacheck"   %% "scalacheck"     % "1.14.0",
  "org.scalactic"    %% "scalactic"      % "3.0.5",
  "org.scalatest"    %% "scalatest"      % "3.0.5",
  "com.ironcorelabs" %% "cats-scalatest" % "2.4.0",
).map(_ % Test)

// ### Projects ###

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, `json-parser`, api)
    .dependsOn(core, `json-parser`, api)

lazy val core =
  project
    .settings(moduleName := projectName)
    .settings(
      libraryDependencies ++= Seq() ++ testKitLibs
    )

lazy val api =
  project
    .settings(moduleName := s"$projectName-api")
    .settings(commonSettings: _*)
    .settings(addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"))
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s ++ circe
    )
    .dependsOn(`json-parser`)

lazy val `json-parser` =
  project
    .settings(moduleName := s"$projectName-json-parser")
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(squants) ++ circe
    )

// ### Commons ###

lazy val commonSettings =
  Seq(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
    libraryDependencies ++= Seq() ++ testKitLibs
  )

/**
  * Copied from Cats
  */
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)
