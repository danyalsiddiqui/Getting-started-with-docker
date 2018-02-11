
lazy val akkaHttpVersion        = "10.0.10"
lazy val akkaVersion            = "2.5.7"
lazy val akkaHttpJson4sVersion  = "1.18.0"
lazy val typesafeConfigVersion  = "1.3.2"
lazy val json4sVersion          = "3.6.0-M1"
lazy val jawnVersion            = "0.11.0"
lazy val sprayVersion="1.3.3"

name := "consumer-service"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "com.typesafe.akka"           %%  "akka-http"                % akkaHttpVersion,
  "com.typesafe.akka"           %% "akka-actor"                 %"2.3.6",
  "com.typesafe"                 % "config"                   % typesafeConfigVersion,
  "com.newmotion"               %% "akka-rabbitmq"            % "5.0.0",
  "com.softwaremill.sttp"       %% "core"                     % "1.1.3",
  "com.typesafe.akka"           %% "akka-stream"              % akkaVersion,
  "com.typesafe.akka"           %% "akka-remote"              % akkaVersion,
  "com.lightbend.akka"          %% "akka-stream-alpakka-amqp" % "0.14",
  "org.postgresql"              % "postgresql"                % "9.3-1102-jdbc41",
  "org.slf4j"                   % "slf4j-nop"                 % "1.6.4",
  "net.liftweb" %% "lift-json" % "3.0.1"
)

mainClass in Compile := Some("akka.rabbitmq.rabbitmq")

