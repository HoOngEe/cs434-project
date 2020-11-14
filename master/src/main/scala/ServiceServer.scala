import scala.concurrent.ExecutionContext
import org.apache.logging.log4j.scala.{Logging, Logger}

import io.grpc.{Server, ServerBuilder}
import service.ConnectServiceGrpc

object ServiceServer extends Logging {
  def start(): Unit = {
    val server = new ServiceServer(ExecutionContext.global, logger)
    server.start()
    server.blockUntilShutdown()
  }

  private val hostAddress = Util.hostAddress
  private val port = 50051
  private lazy val stringAddress = s"${hostAddress}:${port}"
}

class ServiceServer(executionContext: ExecutionContext, logger: Logger) { self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    val connectService = ConnectServiceGrpc.bindService(ConnectServiceImpl, executionContext)
    server = ServerBuilder.forPort(ServiceServer.port).addService(connectService).build.start
    logger.info(s"Connection server started, ${ServiceServer.stringAddress}")
    sys.addShutdownHook{
      logger.error("*** shutting down gRPC connection server since JVM is shutting down")
      stop()
      logger.error("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }
}