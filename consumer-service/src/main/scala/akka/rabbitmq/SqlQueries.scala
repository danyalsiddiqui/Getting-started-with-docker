package akka.rabbitmq

object SqlQueries {
  var InsertRow : String = "INSERT INTO RabbitMqMessage (message) VALUES (?)";
  var SelectAll : String = "SELECT * FROM  RabbitMqMessage";
  var CreateTable :String = "CREATE TABLE RabbitMqMessage (  Id    BIGSERIAL PRIMARY KEY ,    message   varchar(100) );"
  var TableAlreadyExist :String = "SELECT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = ?) as tableStatus;"
  //def TableAlreadyExist(tableName : String) : String = "SELECT * FROM pg_tables WHERE tablename = '"+tableName+"'"
}

