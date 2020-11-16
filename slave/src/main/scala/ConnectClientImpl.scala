import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import org.apache.logging.log4j.scala.Logging
import java.util.concurrent.TimeUnit

import com.google.protobuf.ByteString
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

  /* TODO: remove this snippet */
  val r = scala.util.Random
  def randomByte(): Int = r.nextInt(255)
  def randomKey(): List[Byte] = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0) map (_ => randomByte().toByte)

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def connect(): Unit = {
    val sampleRandomKey = randomKey()
    logger.info(s"Connect to the master, sampled key: ${sampleRandomKey map (_ & 0xff)}")
    val request = sourceAddr(ipv4 = hostAddress, port, sampleKey = ByteString.copyFrom(sampleRandomKey.toArray))
    try {
      blockingStub.connect(request)
    } catch {
      case e: StatusRuntimeException =>
        logger.warn(s"RPC failed: ${e.getStatus}")
    }
  }
}
