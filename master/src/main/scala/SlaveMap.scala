import Common.Key._

object SlaveMap {
  def constructRange(sortedSlaves: List[Slave]): List[(Key, Key)] = {
    val sortedKeys = (sortedSlaves map (_.sampleKey)) dropRight 1
    (zeroKey :: sortedKeys) zip (sortedKeys ::: List(maxKey))
  }

  class SlaveMap(slaves: List[Slave]) {
    val sortedSlaves = slaves.sorted
    val keyRanges = constructRange(sortedSlaves)
    val slaveMap =
      (keyRanges zip sortedSlaves) map { case (keyRange, slave) => keyRange -> slave } toMap

    private def searchRange(key: Key): (Key, Key) = keyRanges.find({case (_, end) => key < end}) get
    def apply(key: Key): Slave = slaveMap get (searchRange(key)) get
  }
}
