
organization := "name.heikoseeberger.akkaexamples"

name := "akkaexamples"

// version is defined in version.sbt in order to support sbt-release

resolvers += "Akka Repository" at "http://akka.io/repository"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0-RC1",
  "net.databinder" %% "unfiltered-netty-server" % "0.5.3"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

publishTo <<= (version) { version =>
  def hseeberger(name: String) =
    Resolver.file(
      "hseeberger-%s" format name,
      file("/Users/heiko/projects/hseeberger.github.com/%s" format name)
    )(Resolver.ivyStylePatterns)
  val resolver =
    if (version endsWith "SNAPSHOT") hseeberger("snapshots")
    else hseeberger("releases")
  Option(resolver)
}

publishMavenStyle := false

seq(scalariformSettings: _*)
