import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._

object Build extends Build {

  lazy val root = Project(
    "akkaexamples",
    file("."),
    settings = Defaults.defaultSettings ++
      scalariformSettings ++
      Seq(
        organization := "name.heikoseeberger",
        // version is defined in version.sbt to support sbt-release
        scalaVersion := "2.10.0-RC1",
        scalaBinaryVersion := "2.10.0-RC1",
        scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_"),
        resolvers ++= Seq(
          "spray repo" at "http://repo.spray.io",
          Opts.resolver.sonatypeSnapshots
        ),
        libraryDependencies ++= Seq(
          Dependencies.Compile.SprayCan,
          Dependencies.Compile.SprayRouting,
          Dependencies.Compile.AkkaSlf4j,
          Dependencies.Compile.LogbackClassic,
          Dependencies.Test.ScalaTest,
          Dependencies.Test.ScalaCheck,
          Dependencies.Test.ScalaMock
        ),
        initialCommands in console := "import name.heikoseeberger.akkaexamples._",
      )
  )

  object Dependencies {

    object Compile {
      //      val Config = "com.typesafe" % "config" % "1.0.0"
      val SprayCan = "io.spray" % "spray-can" % "1.1-M4.2"
      val SprayRouting = "io.spray" % "spray-routing" % "1.1-M4.2"
      val AkkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.1.0-RC1"
      val LogbackClassic = "ch.qos.logback" % "logback-classic" % "1.0.7"
    }

    object Test {
      val ScalaTest = "org.scalatest" %% "scalatest" % "2.0.M4" % "test"
      val ScalaCheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
      val ScalaMock = "org.scalamock" %% "scalamock-scalatest-support" % "3.0-M5" % "test"
    }
  }
}
