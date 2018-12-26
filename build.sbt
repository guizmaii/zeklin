ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true
ThisBuild / version := "0.1"

lazy val projectName = "zeklin"

// ### Dependencies ###

lazy val circe = ((version: String) =>
  Seq(
    "io.circe" %% "circe-core"    % version,
    "io.circe" %% "circe-generic" % version,
    "io.circe" %% "circe-parser"  % version
  ))("0.11.0")

lazy val testKitLibs = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.0",
  "org.scalactic"  %% "scalactic"  % "3.0.5",
  "org.scalatest"  %% "scalatest"  % "3.0.5",
  "com.ironcorelabs" %% "cats-scalatest" % "2.4.0",
).map(_ % Test)

// ### Projects ###

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, `json-parser`)
    .dependsOn(core, `json-parser`)

lazy val core =
  project
    .settings(moduleName := projectName)
    .settings(
      libraryDependencies ++= Seq() ++ testKitLibs
    )

lazy val `json-parser` =
  project
    .settings(moduleName := s"$projectName-json-parser")
    .settings(
      libraryDependencies ++= Seq() ++ circe ++ testKitLibs
    )

// ### Commons ###

/**
  * Copied from Cats
  */
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)
