import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import org.apache.logging.log4j.scala.Logging
import java.util.concurrent.TimeUnit

import service.ConnectServiceGrpc.ConnectServiceBlockingStub
import service.{ConnectServiceGrpc, sourceAddr}

object ConnectClient {
  def apply(host: String, port: Int): ConnectClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val blockingStub = ConnectServiceGrpc.blockingStub(channel)
    new ConnectClient(channel, blockingStub)
  }
}

class ConnectClient private(
  private val channel: ManagedChannel,
  private val blockingStub: ConnectServiceBlockingStub
) extends Logging {
  val hostAddress = Util.hostAddress
  val port = 5000

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def connect(): Unit = {
    logger.info("Connect to the master")
    val request = sourceAddr(ipv4 = hostAddress, port)
    try {
      blockingStub.connect(request)
    } catch {
      case e: StatusRuntimeException =>
        logger.warn(s"RPC failed: ${e.getStatus}")
    }
  }
}
