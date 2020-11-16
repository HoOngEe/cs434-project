package Common

class Key(private val byteList: List[Byte]) {
  require(byteList.length == 10)

  private object KeyOrdering extends Ordering[Key] {
    override def compare(x: Key, y: Key): Int = {
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
      compare_aux(x.byteList, y.byteList)
    }
  }

  @inline def <(other: Key): Boolean = KeyOrdering.compare(this, other) < 0
  @inline def >(other: Key): Boolean = KeyOrdering.compare(this, other) > 0
  @inline def ==(other: Key): Boolean = KeyOrdering.compare(this, other) == 0
  @inline def >=(other: Key): Boolean = this > other || this == other
  @inline def <=(other: Key): Boolean = this < other || this == other
}
