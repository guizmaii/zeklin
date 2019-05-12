import sbt.Keys.libraryDependencies

ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true
ThisBuild / version := "0.1"

lazy val projectName = "zeklin"

// ### Dependencies ###

lazy val squants            = "org.typelevel"         %% "squants"                 % "1.4.0"
lazy val logback            = "ch.qos.logback"        % "logback-classic"          % "1.2.3"
lazy val zio                = "org.scalaz"            %% "scalaz-zio"              % "1.0-RC4"
lazy val `zio-cats-interop` = "org.scalaz"            %% "scalaz-zio-interop-cats" % "1.0-RC4"
lazy val `cats-effects`     = "org.typelevel"         %% "cats-effect"             % "1.3.0"
lazy val h2                 = "com.h2database"        % "h2"                       % "1.4.199"
lazy val flyway             = "org.flywaydb"          % "flyway-core"              % "5.2.4"
lazy val pureconfig         = "com.github.pureconfig" %% "pureconfig"              % "0.11.0"

lazy val doobie = ((version: String) =>
  Seq(
    "org.tpolecat" %% "doobie-core"   % version,
    "org.tpolecat" %% "doobie-h2"     % version,
    "org.tpolecat" %% "doobie-hikari" % version,
  ))("0.6.0")

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
  ))("0.20.0")

lazy val testKitLibs = Seq(
  "org.scalacheck"   %% "scalacheck"     % "1.14.0",
  "org.scalactic"    %% "scalactic"      % "3.0.7",
  "org.scalatest"    %% "scalatest"      % "3.0.7",
  "com.ironcorelabs" %% "cats-scalatest" % "2.4.0",
).map(_ % Test)

// ### Commons ###

lazy val commonSettings =
  Seq(
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0"),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.10"),
    libraryDependencies ++= Seq(
      `cats-effects`,
      zio,
      `zio-cats-interop`,
      pureconfig,
      flyway,
      h2,
    ) ++ doobie ++ testKitLibs
  )

// ### Projects ###

lazy val root =
  Project(id = projectName, base = file("."))
    .settings(moduleName := "root")
    .settings(noPublishSettings: _*)
    .aggregate(core, `json-parser`, server, `api-public`, `api-private`, accounts, `test-kit`)
    .dependsOn(core, `json-parser`, server, `api-public`, `api-private`, accounts, `test-kit`)

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
    .settings(mainClass in Compile := Some("com.guizmaii.zeklin.api.Server"))
    .dependsOn(`api-public`, `api-private`)
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

// ### Others ###

/**
  * Copied from Cats
  */
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)
