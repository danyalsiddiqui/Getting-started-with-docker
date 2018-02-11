package akka.rabbitmq

import com.newmotion.akka.rabbitmq._
import akka.actor

import java.io.{InputStream, OutputStream}
import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

object rabbitmq extends  App{
  var exchange = "test.Exchange";
  var queue="test.Exchange.queue";
  var connectionUrl=if (sys.env("RABBITMQ_HOST") != null)  sys.env("RABBITMQ_HOST") else "amqp://guest:guest@localhost:5672";
  val Dbcontext = new StoreData()
  //  val message =
   Dbcontext.CreateDbTableIfNotExist("RabbitMqMessage");
  //  Dbcontext.Insert(message)
  val factory = new ConnectionFactory()
  factory.setUri(connectionUrl)
  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  channel.exchangeDeclare(exchange,"direct", true)
  channel.queueDeclare(queue, false, false, false, null)
  val consumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) {
      Dbcontext.Insert(new Message(fromBytes(body)))
      println("received: " + fromBytes(body))
      println(Dbcontext.GetAllData().toString())
    }
  }
  channel.basicConsume(queue, true, consumer)

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")


//  val Dbcontext = new StoreData()
//  val message = new Message("HEllo World")
//  Dbcontext.CreateDbTableIfNotExist("RabbitMqMessage");
//  Dbcontext.Insert(message)
//
//  val a =Dbcontext.GetAllData();

  val server = HttpServer.create(new InetSocketAddress(8000),0)
  server.createContext("/", new RootHandler())
  server.setExecutor(null)

  server.start()

  println("Open localHost:8000 to get the data & Hit any key to exit...")

  System.in.read()
  server.stop(0)
}


class RootHandler extends HttpHandler {

  def handle(t: HttpExchange) {
    sendResponse(t)
  }

  private def sendResponse(t: HttpExchange) {
    val Dbcontext = new StoreData()

    val response = Dbcontext.GetAllData().toString
    t.sendResponseHeaders(200, response.length())
    val os = t.getResponseBody
    os.write(response.getBytes)
    os.close()
  }

}





