package akka.rabbitmq

import com.newmotion.akka.rabbitmq._

object rabbitmq extends  App{
  /*var exchange = "test.Exchange";
  var queue="test.Exchange.queue";
  var connectionUrl=if (sys.env("RABBITMQ_HOST") != null)  sys.env("RABBITMQ_HOST") else "amqp://guest:guest@localhost:5672";

  val factory = new ConnectionFactory()
  factory.setUri(connectionUrl)
  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  channel.exchangeDeclare(exchange,"direct", true)
  channel.queueDeclare(queue, false, false, false, null)
  val consumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) {
      println("received: " + fromBytes(body))
    }
  }
  channel.basicConsume(queue, true, consumer)

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")
*/

  val Dbcontext = new StoreData()
  Dbcontext.SaveDataInPostgress("HEllo World")
}





