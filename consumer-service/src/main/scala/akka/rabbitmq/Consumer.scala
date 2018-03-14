package akka.rabbitmq

import com.newmotion.akka.rabbitmq._
import com.typesafe.config.{Config, ConfigFactory}
import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

object Consumer extends  App{

  //variable declaration
  private val config: Config = ConfigFactory.load()
  var exchange = config.getString("rabbitmq.exchangeName")
  var queue = config.getString("rabbitmq.queueName")
  var connectionUrl= config.getString("rabbitmq.hostname")
  val dbTableName = config.getString("postgres.dbTableName")
  val factory = new ConnectionFactory()
  val Dbcontext = new StoreData()


  // create Table
   Dbcontext.CreateDbTableIfNotExist(dbTableName);


  // setting up RabbitMq
  factory.setUri(connectionUrl)
  val connection: Connection = factory.newConnection()
  val channel: Channel = connection.createChannel()
  channel.exchangeDeclare(exchange,"direct", true)
  channel.queueDeclare(queue, false, false, false, null)
  channel.queueBind(queue, exchange,"")

  val consumer = new DefaultConsumer(channel) {
    override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) {
      Dbcontext.Insert(new Message(fromBytes(body)))
    }
  }
  channel.basicConsume(queue, true, consumer)

  def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")


  //Setting Up the server

  val server = HttpServer.create(new InetSocketAddress(8000),0)
  server.createContext("/", new RootHandler())
  server.setExecutor(null)

  server.start()

  println("Open localHost:8000 to get the data & Hit any key to exit...")

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





