ThisBuild / organization := "com.guizmaii"
ThisBuild / scalaVersion := "2.12.10"
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
)("0.8.4")

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
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .aggregate(core, `json-parser`, server, `api-public`, `api-private`, accounts, github, frontend, `test-kit`)
    .dependsOn(core, `json-parser`, server, `api-public`, `api-private`, accounts, github, frontend, `test-kit`)

lazy val core =
  project
    .settings(moduleName := projectName)
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(
      libraryDependencies ++= Seq() ++ testKitLibs
    )

lazy val server =
  project
    .settings(moduleName := s"$projectName-server")
    .enablePlugins(WebScalaJSBundlerPlugin, SbtWeb, JavaAppPackaging, BuildEnvPlugin)
    .settings(noDoc: _*)
    .settings(
      reStart / mainClass := Some("com.guizmaii.zeklin.api.Server"),
      Revolver.enableDebugging()
    )
    .settings(commonSettings: _*)
    .settings(
      scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(logback) ++ http4s
    )
    .settings(
      // https://scalacenter.github.io/scalajs-bundler/getting-started.html#sbt-web
      scalaJSProjects := Seq(frontend),
      scalaJSPipeline / devCommands ++= Seq("~reStart", "~compile", "~test:compile", "set", "session", "*/*:dumpStructureTo"),
      Assets / pipelineStages := Seq(scalaJSPipeline),
      Assets / WebKeys.packagePrefix := "public/",
      Assets / WebKeys.exportedMappings := (
        for ((file, path) <- (Assets / mappings).value)
          yield file -> ((Assets / WebKeys.packagePrefix).value + path)
      ),
      Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    )
    .dependsOn(modules, `api-public`, `api-private`, github)
    .dependsOn(`test-kit` % Test)

lazy val frontend =
  project
    .settings(moduleName := s"$projectName-frontend")
    .enablePlugins(ScalaJSBundlerPlugin)
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(
      resolvers += "jitpack" at "https://jitpack.io",
      libraryDependencies ++= Seq(
        "com.github.OutWatch.outwatch" %%% "outwatch"  % "b07808cb12",
        "org.scalatest"                %%% "scalatest" % "3.0.8" % Test
      ),
      Compile / npmDependencies += "bulma" -> "0.7.5"
    )
    .settings(
      scalacOptions += "-P:scalajs:sjsDefinedByDefault",
      useYarn := true, // makes scalajs-bundler use yarn instead of npm
      Test / requireJsDomEnv := true,
      scalaJSUseMainModuleInitializer := true,
      fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly(),
      // configure Scala.js to emit a JavaScript module instead of a top-level script
      scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
      emitSourceMaps := false,
    )

lazy val `api-public` =
  project
    .settings(moduleName := s"$projectName-api-outer")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(logback) ++ http4s ++ circe
    )
    .dependsOn(`json-parser`)
    .dependsOn(`test-kit` % Test)

lazy val `api-private` =
  project
    .settings(moduleName := s"$projectName-api-inner")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
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
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)

lazy val github =
  project
    .settings(moduleName := s"$projectName-github")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)
    .settings(
      //scalacOptions := scalacOptions.value.filter(_ != "-Xfatal-warnings"),
      libraryDependencies ++= Seq(`jwt-circe`) ++ http4s ++ circe
    )
    .dependsOn(modules)

lazy val modules =
  project
    .settings(moduleName := s"$projectName-modules")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)

lazy val `json-parser` =
  project
    .settings(moduleName := s"$projectName-json-parser")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(squants) ++ circe
    )
    .dependsOn(`test-kit` % Test)

lazy val `test-kit` =
  project
    .settings(moduleName := s"$projectName-test-kit")
    .disablePlugins(RevolverPlugin)
    .settings(noMain: _*)
    .settings(noDoc: _*)
    .settings(commonSettings: _*)

// ### Others ###

lazy val noMain = Seq(
  reStart / mainClass := None
)

lazy val noDoc = Seq(
  sources in (Compile, doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false
)
