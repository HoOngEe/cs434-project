package Common

object Key {
  val zeroKey = new Key(List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
  val maxKey = new Key(List(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1))

  class Key(private val byteList: List[Byte]) extends Ordered[Key] {
    require(byteList.length == 10)

    override def toString = byteList map (_ & 0xff) toString

    override def compare(other: Key): Int = {
      def compare_aux(x: List[Byte], y: List[Byte]): Int = {
        (x, y) match {
          case (List(), List()) => 0
          case (xFst::xTail, yFst::yTail) => {
            val fstCompared = (xFst & 0xff) compare (yFst & 0xff)
            if (fstCompared == 0) compare_aux(xTail, yTail) else fstCompared
          }
          case _ => throw new IllegalArgumentException("lengths must be same")
        }
      }
      compare_aux(this.byteList, other.byteList)
    }
  }
}

