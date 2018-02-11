package akka.rabbitmq
import java.sql.{Connection, DriverManager, ResultSet}
import org.postgresql.jdbc4._;
import javax.sql.DataSource
import scala.collection.mutable._

import scala.collection.mutable.ListBuffer



trait  SaveDataInDb{
  def Insert (message : Message)
  def CreateDbTableIfNotExist (tableName : String )
  def GetAllData ( ) : ListBuffer[Message]
}



class StoreData extends  SaveDataInDb {

  implicit class RsExtender(rs: ResultSet) {

    def toList[T](row: ResultSet => T): List[T] = {
      var buffer = ListBuffer[T]()
      try {
        while (rs.next()) {
          buffer += row(rs)
        }
      } finally {
        rs.close()
      }
      buffer.toList
    }

  }

  classOf[org.postgresql.Driver]
  val conn_str = "jdbc:postgresql://"+sys.env("POSTGRES_HOST")+"/"+sys.env("POSTGRES_DB")+"?user="+sys.env("POSTGRES_USER")+"&password="+sys.env("POSTGRES_PASSWORD");
  val connection = DriverManager.getConnection(conn_str)

  def Insert (message : Message) = {

    try
    {
      val stm = connection.prepareStatement(SqlQueries.InsertRow)
      stm.setString(1,message.body)
      val rs = stm.executeUpdate();
    }
    catch
    {
      case t: Throwable => println(s"Postgres Crashed due to exception: $t")
    }
    finally
    {
      //connection.close()
    }

  }


  def CreateDbTableIfNotExist (tableName : String ) = {
    try
    {
      val stm = connection.prepareStatement(SqlQueries.CreateTable);
      stm.execute();
    }
    catch
    {
      case t: Throwable => println(s"Postgres Crashed due to exception: $t")
    }
    finally
    {
      //connection.close()
    }
  }

  def GetAllData() : ListBuffer[Message] ={

    val stm = connection.prepareStatement(SqlQueries.SelectAll)
    var list = ListBuffer[Message]();
     try
     {
       val rs = stm.executeQuery();
       while(rs.next())
       {
         list += Message( rs.getString("Message"),rs.getInt("Id"))
       }
        return list
     }
     catch
     {
        case t: Throwable => println(s"Postgres Crashed due to exception: $t")
         return list
     }
     finally
     {
       if (stm != null) { stm.close(); }
     }
  }

}
