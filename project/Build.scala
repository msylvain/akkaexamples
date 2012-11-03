import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._

object Build extends Build {

  lazy val root = Project(
    "akkaexamples",
    file("."),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= Seq(
      )
    )
  )

  def commonSettings = 
    Defaults.defaultSettings ++ 
    scalariformSettings ++
    Seq(
      organization := "name.heikoseeberger",
      // version is defined in version.sbt to support sbt-release
      scalaVersion := "2.10.0-RC1",
      scalaBinaryVersion := "2.10.0-RC1",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_"),
      libraryDependencies ++= Seq(
        Dependencies.Test.ScalaTest,
        Dependencies.Test.ScalaCheck,
        Dependencies.Test.ScalaMock
      ),
      initialCommands in console := "import name.heikoseeberger.akkaexamples._"
    )

  object Dependencies {

    object Compile {
      val Config = "com.typesafe" % "config" % "1.0.0"
    }

    object Test {
      val ScalaTest = "org.scalatest" %% "scalatest" % "2.0.M4" % "test"
      val ScalaCheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
      val ScalaMock = "org.scalamock" %% "scalamock-scalatest-support" % "3.0-M5" % "test"
    }
  }
}
