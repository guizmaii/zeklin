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
    "io.circe" %% "circe-parser"  % version,
    "io.circe" %% "circe-fs2"     % "0.11.0",
  ))("0.11.1")

lazy val http4s = ((version: String) =>
  Seq(
    "org.http4s" %% "http4s-blaze-server" % version,
    "org.http4s" %% "http4s-circe"        % version,
    "org.http4s" %% "http4s-dsl"          % version,
  ))("0.20.0-RC1")

lazy val testKitLibs = Seq(
  "org.scalacheck"   %% "scalacheck"     % "1.14.0",
  "org.scalactic"    %% "scalactic"      % "3.0.7",
  "org.scalatest"    %% "scalatest"      % "3.0.7",
  "com.ironcorelabs" %% "cats-scalatest" % "2.4.0",
).map(_ % Test)

// ### Projects ###

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, `json-parser`, `api-public`, `test-kit`)
    .dependsOn(core, `json-parser`, `api-public`, `test-kit`)

lazy val core =
  project
    .settings(moduleName := projectName)
    .settings(
      libraryDependencies ++= Seq() ++ testKitLibs
    )

lazy val `api-public` =
  project
    .settings(moduleName := s"$projectName-api-publik")
    .settings(commonSettings: _*)
    .settings(addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10"))
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s ++ circe
    )
    .settings(mainClass in Compile := Some("com.guizmaii.zeklin.api.publik.Server"))
    .dependsOn(`json-parser`)
    .dependsOn(`test-kit` % Test)

lazy val `json-parser` =
  project
    .settings(moduleName := s"$projectName-json-parser")
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(squants) ++ circe
    )
    .dependsOn(`test-kit` % Test)

lazy val `test-kit` =
  project
    .settings(moduleName := s"$projectName-test-kit")
    .settings(noPublishSettings: _*)
    .settings(commonSettings: _*)

// ### Commons ###

lazy val commonSettings =
  Seq(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0"),
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
