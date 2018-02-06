package akka.rabbitmq
import java.sql.{Connection, DriverManager, ResultSet};

class StoreData {
  val conn_str = "jdbc:postgressql:postgresDB" ;
  def SaveDataInPostgress (stringToStore : String) = {
    classOf[org.postgresql.Driver]
    val conn = DriverManager.getConnection(conn_str)
    try
    {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)

      val rs = stm.executeQuery("SELECT * from Users")

      while(rs.next) {
        println(rs.getString("quote"))
      }
    }
    catch
    {
      case _: Throwable => println("Postgres Crashed due to exception")
    }
    finally
    {
      conn.close()
    }

  }

}
