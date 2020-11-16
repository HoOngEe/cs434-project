package Common

import org.scalatest.FunSuite

class KeyTest extends FunSuite {
  test("ByteList with length other than 10 must not construct key") {
    val byteListLen9: List[Byte] = List(1, 1, 1, 1, 1, 1, 1, 1, 1)
    try {
      new Key(byteListLen9)
    } catch {
      case _: IllegalArgumentException => assert(true)
    }
  }

  test(" Compare keys") {
    val keySmaller = new Key(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    val keyBigger = new Key(List(1, 2, 3, 4 ,5, 6, 7, 8, 9, 11))
    assert(keySmaller < keyBigger)
  }
}
