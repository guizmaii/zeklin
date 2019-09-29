import sbt.Keys.libraryDependencies
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true
ThisBuild / version := "0.1"

lazy val projectName = "zeklin"

// ### Dependencies ###

lazy val squants            = "org.typelevel"         %% "squants"          % "1.5.0"
lazy val logback            = "ch.qos.logback"        % "logback-classic"   % "1.2.3"
lazy val zio                = "dev.zio"               %% "zio"              % "1.0.0-RC13"
lazy val `zio-cats-interop` = "dev.zio"               %% "zio-interop-cats" % "2.0.0.0-RC4"
lazy val `cats-effects`     = "org.typelevel"         %% "cats-effect"      % "2.0.0"
lazy val h2                 = "com.h2database"        % "h2"                % "1.4.199"
lazy val flyway             = "org.flywaydb"          % "flyway-core"       % "6.0.4"
lazy val pureconfig         = "com.github.pureconfig" %% "pureconfig"       % "0.12.1"
lazy val `jwt-circe`        = "com.pauldijou"         %% "jwt-circe"        % "4.1.0"
lazy val bouncycastle       = "org.bouncycastle"      % "bcprov-jdk15on"    % "1.63"
lazy val `fs2-kafka`        = "com.ovoenergy"         %% "fs2-kafka"        % "0.20.1"

lazy val doobie = (
  (version: String) =>
    Seq(
      "org.tpolecat" %% "doobie-core"   % version,
      "org.tpolecat" %% "doobie-h2"     % version,
      "org.tpolecat" %% "doobie-hikari" % version
    )
)("0.8.2")

lazy val circe = (
  (version: String) =>
    Seq(
      "io.circe" %% "circe-core"           % version,
      "io.circe" %% "circe-generic"        % version,
      "io.circe" %% "circe-parser"         % version,
      "io.circe" %% "circe-generic-extras" % "0.12.2",
      "io.circe" %% "circe-optics"         % "0.12.0",
      "io.circe" %% "circe-literal"        % version % Test
    )
)("0.12.1")

lazy val http4s = (
  (version: String) =>
    Seq(
      "org.http4s" %% "http4s-blaze-server" % version,
      "org.http4s" %% "http4s-blaze-client" % version,
      "org.http4s" %% "http4s-circe"        % version,
      "org.http4s" %% "http4s-dsl"          % version,
      "org.http4s" %% "http4s-scalatags"    % version
    )
)("0.21.0-M5")

lazy val testKitLibs = Seq(
  "org.scalacheck"   %% "scalacheck"     % "1.14.2",
  "org.scalactic"    %% "scalactic"      % "3.0.8",
  "org.scalatest"    %% "scalatest"      % "3.0.8",
  "com.ironcorelabs" %% "cats-scalatest" % "3.0.0"
).map(_ % Test)

// ### Commons ###

lazy val commonSettings =
  Seq(
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    libraryDependencies ++= Seq(
      `cats-effects`,
      zio,
      `zio-cats-interop`,
      pureconfig,
      flyway,
      h2,
      bouncycastle,
      `fs2-kafka`
    ) ++ doobie ++ testKitLibs
  )

// ### Projects ###

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, `json-parser`, server, `api-public`, `api-private`, accounts, github, frontend, `test-kit`)
    .dependsOn(core, `json-parser`, server, `api-public`, `api-private`, accounts, github, frontend, `test-kit`)

lazy val core =
  project
    .settings(moduleName := projectName)
    .settings(
      libraryDependencies ++= Seq() ++ testKitLibs
    )

lazy val server =
  project
    .settings(moduleName := s"$projectName-api-server")
    .settings(commonSettings: _*)
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s
    )
    .settings(
      mainClass in reStart := Some("com.guizmaii.zeklin.api.Server"),
      // Allows to read the generated JS on client
      resources in Compile += (fastOptJS in (frontend, Compile)).value.data,
      // Lets the backend to read the .map file for js
      resources in Compile += (fastOptJS in (frontend, Compile)).value
        .map((x: sbt.File) => new File(x.getAbsolutePath + ".map"))
        .data,
      // Lets the server read the jsdeps file
      (managedResources in Compile) += (artifactPath in (frontend, Compile, packageJSDependencies)).value,
      // do a fastOptJS on reStart
      reStart := (reStart dependsOn (fastOptJS in (frontend, Compile))).evaluated,
      // This settings makes reStart to rebuild if a scala.js file changes on the client
      watchSources ++= (watchSources in frontend).value
    )
    .dependsOn(modules, `api-public`, `api-private`, frontend, sharedJvm)
    .dependsOn(`test-kit` % Test)

lazy val `api-public` =
  project
    .settings(moduleName := s"$projectName-api-outer")
    .settings(commonSettings: _*)
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s ++ circe
    )
    .dependsOn(`json-parser`)
    .dependsOn(`test-kit` % Test)

lazy val `api-private` =
  project
    .settings(moduleName := s"$projectName-api-inner")
    .settings(commonSettings: _*)
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s ++ circe
    )
    .dependsOn(accounts)
    .dependsOn(`test-kit` % Test)

lazy val accounts =
  project
    .settings(moduleName := s"$projectName-accounts")
    .settings(commonSettings: _*)

lazy val github =
  project
    .settings(moduleName := s"$projectName-github")
    .settings(commonSettings: _*)
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(`jwt-circe`) ++ http4s ++ circe
    )

lazy val modules =
  project
    .settings(moduleName := s"$projectName-modules")
    .settings(commonSettings: _*)

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

lazy val frontend =
  project
    .enablePlugins(ScalaJSPlugin)
    .settings(moduleName := s"$projectName-frontend")
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.passay"   % "passay"        % "1.5.0",
        "org.scala-js" %%% "scalajs-dom" % "0.9.7",
      ) ++ http4s
    )
    .settings(
      // Build a js dependencies file
      skip in packageJSDependencies := false,
      jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
      // Put the jsdeps file on a place reachable for the server
      crossTarget in (Compile, packageJSDependencies) := (resourceManaged in Compile).value
    )
    .dependsOn(modules, sharedJs, github)

lazy val shared =
  (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("shared"))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "scalatags" % "0.7.0"
      ) ++ circe
    )

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

// ### Others ###

/**
 * Copied from Cats
 */
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)
