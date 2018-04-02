organization := "takumon"
name := "gitbucket-issue-estimation-plugin"
version := "0.1"
scalaVersion := "2.12.4"
gitbucketVersion := "4.20.0"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")
jarName in assembly := { s"${name.value}-assembly-${version.value}.jar" }

useJCenter := true

libraryDependencies ++= Seq(
  "io.github.gitbucket" %% "gitbucket"                % "4.20.0"  % "provided",
  "javax.servlet"        % "javax.servlet-api"        % "3.1.0"   % "provided",
)
