package Common

class Key(private val byteList: List[Byte]) extends Ordered[Key] {
  require(byteList.length == 10)

  override def compare(other: Key): Int = {
    def compare_aux(x: List[Byte], y: List[Byte]): Int = {
      (x, y) match {
        case (List(), List()) => 0
        case (xFst::xTail, yFst::yTail) => {
          val fstCompared = xFst compare yFst
          if (fstCompared == 0) compare_aux(xTail, yTail) else fstCompared
        }
        case _ => throw new IllegalArgumentException("lengths must be same")
      }
    }
    compare_aux(this.byteList, other.byteList)
  }
}
