import Common.Key._

class Slave(val addr: String,
            val port: Int,
            val sampleKey: Key) extends Ordered[Slave] {
  override def compare(other: Slave): Int = {
    this.sampleKey compare other.sampleKey
  }
}
