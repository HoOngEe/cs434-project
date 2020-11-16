import java.net.InetAddress

object Util {
  val hostAddress = InetAddress.getLocalHost.getHostAddress
}