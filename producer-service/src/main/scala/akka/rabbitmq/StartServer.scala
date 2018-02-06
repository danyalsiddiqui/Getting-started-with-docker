package akka.rabbitmq

import com.newmotion.akka.rabbitmq._

object StartServer  extends App{
  var exchange = "test.Exchange";
  var queue="test.Exchange.queue";
  var connectionUrl= if (sys.env("RABBITMQ_HOST") != null)  sys.env("RABBITMQ_HOST") else "amqp://guest:guest@localhost:5672";
  var messege="Hello World from producer";

  val factory = new ConnectionFactory()
  factory.setUri(connectionUrl)
  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  channel.exchangeDeclare(exchange,"direct", true)
  channel.queueDeclare(queue, false, false, false, null)

  channel.basicPublish("",queue, null, messege.getBytes)
  //connection.close()
  channel.close()
}
