package akka.rabbitmq

object SqlQueries {
  var InsertRow : String = "INSERT INTO RabbitMqMessage (message) VALUES (?)";
  var SelectAll : String = "SELECT * FROM  RabbitMqMessage";
  var CreateTable :String = "CREATE TABLE RabbitMqMessage (  Id    BIGSERIAL PRIMARY KEY ,    message   varchar(100) );";
}

