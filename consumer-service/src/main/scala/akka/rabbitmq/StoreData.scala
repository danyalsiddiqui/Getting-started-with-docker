package akka.rabbitmq

import java.sql.{Connection, DriverManager, ResultSet}
import com.typesafe.config.{Config, ConfigFactory}
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
  private val config: Config = ConfigFactory.load()
  private val postgresHost = config.getString("postgres.hostName")
  private val postgresDB = config.getString("postgres.db")
  private val postgresUser = config.getString("postgres.user")
  private val postgresPassword = config.getString("postgres.password")


  val conn_str = "jdbc:postgresql://"+postgresHost+"/"+postgresDB+"?user="+postgresUser+"&password="+postgresPassword;
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
      var query = SqlQueries.TableAlreadyExist  //Table Exist query
      var statment = connection.prepareStatement(query)
      statment.setString(1,tableName.toLowerCase()) // Adding table name as parameter in the statment
      var rs = statment.executeQuery()
      if(! rs.next()) // if the result is empty then create new table
      {
        val stm = connection.prepareStatement(SqlQueries.CreateTable) //create table
        stm.execute();
      }

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
