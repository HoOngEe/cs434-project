import org.apache.logging.log4j.scala.Logging
import service.{ConnectServiceGrpc, connectionResult, sourceAddr}

import scala.concurrent.Future
import SlaveMap._
import Common.Key._

object ConnectServiceImpl extends ConnectServiceGrpc.ConnectService with Logging {
  var slaves: Array[Slave] = Array()

  override def connect(request: sourceAddr): Future[connectionResult] = {
    logger.info(s"${request.ipv4} : ${request.port} requested connection")
    slaves = slaves :+ new Slave(request.ipv4, request.port, new Key(request.sampleKey.toByteArray.toList))
    if (slaves.length == 3) {
      val slaveMap = new SlaveMap(slaves.toList)
      logger.info(slaveMap.keyRanges)
    }
    Future.successful(connectionResult(result = true))
  }
}
