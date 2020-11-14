object Main extends App {
  override def main(args: Array[String]): Unit = {
    val client = ConnectClient(args.head, 50051)
    try {
      client.connect()
    } finally {
      client.shutdown()
    }
  }
}