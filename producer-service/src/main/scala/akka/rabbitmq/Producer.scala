package akka.rabbitmq

import com.newmotion.akka.rabbitmq._
import com.typesafe.config.{Config, ConfigFactory}

object Producer  extends App{
  private val config: Config = ConfigFactory.load()
  private val hostname: String = config.getString("rabbitmq.hostname")
  private val exchange : String = config.getString("exchange.exchangeName")

  var connectionUrl=hostname
  var messege="Hello World from producer"

  val factory = new ConnectionFactory()
  factory.setUri(connectionUrl)
  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  channel.exchangeDeclare(exchange,"direct", true)

  while (true) {
    channel.basicPublish(exchange, "", null, messege.getBytes)
    Thread.sleep(5000)
  }

}
