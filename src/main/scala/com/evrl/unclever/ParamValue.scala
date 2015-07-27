package com.evrl.unclever

import java.sql.PreparedStatement
import scala.language.implicitConversions

/** A typeclass for parameter values */
trait ParamValue[A] {
  def bindIn(s: PreparedStatement, i: Int)
}


/**
 * All instances of the ParamValue type class.
 */
// See rant in DbValue
object ParamValue {

  // Kept alphabetically

  implicit def boolean2paramValue(v: Boolean): ParamValue[Boolean]  = new ParamValue[Boolean] {
    override def bindIn(s: PreparedStatement, i: Int): Unit =
      s.setBoolean(i, v)
  }
  implicit def blob2paramValue(v: Array[Byte]): ParamValue[Array[Byte]] = new ParamValue[Array[Byte]] {
    override def bindIn(s: PreparedStatement, i: Int): Unit =
      s.setBytes(i, v)
  }
  implicit def int2paramValue(v: Int): ParamValue[Int]  = new ParamValue[Int] {
    override def bindIn(s: PreparedStatement, i: Int): Unit =
      s.setInt(i, v)
  }
  implicit def long2paramValue(v: Long): ParamValue[Long]  = new ParamValue[Long] {
    override def bindIn(s: PreparedStatement, i: Int): Unit =
      s.setLong(i, v)
  }
  implicit def string2paramValue(v: String): ParamValue[String] = new ParamValue[String] {
    override def bindIn(s: PreparedStatement, i: Int): Unit =
      s.setString(i, v)
  }

}
