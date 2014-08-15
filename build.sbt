organization := "com.optrak"

name := "eventstore-test"

version := "0.1.0-SNAPSHOT"

resolvers ++= {
  Seq(
    "sonatype.releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "SS" at "http://oss.sonatype.org/content/repositories/snapshots/",
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "Geotools" at "http://download.osgeo.org/webdav/geotools/"
  )
}


scalaVersion := "2.11.1"

libraryDependencies ++= {
    Seq(
      "joda-time" % "joda-time" % "2.4",
      "org.joda" % "joda-convert" % "1.7",
      "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.4",
      "com.geteventstore" %% "akka-persistence-eventstore" % "0.0.2",
      "org.json4s" %% "json4s-native" % "3.2.10",
      "org.json4s" %% "muster-core" % "latest.release",
      "org.json4s" %% "muster-codec-json4s" % "0.3.0",
      "org.specs2" %% "specs2" % "2.3.12" % "test"
    )
}

parallelExecution in Test := true
