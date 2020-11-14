import org.apache.logging.log4j.scala.Logging
import service.{ConnectServiceGrpc, connectionResult, sourceAddr}

import scala.concurrent.Future

object ConnectServiceImpl extends ConnectServiceGrpc.ConnectService with Logging {
  override def connect(request: sourceAddr): Future[connectionResult] = {
    logger.info(s"${request.ipv4} : ${request.port} requested connection")
    Future.successful(connectionResult(result = true))
  }
}
