package com.evrl.unclever

import java.sql.ResultSet
import scala.language.implicitConversions

/** A type class for database values */
trait DbValue[A] {
  def value(r: ResultSet, i: Int): A
}

/**
 * All default instances of the DbValue type class.
 */
object DbValue {

  // Kept alphabetically

  implicit object BooleanDbValue extends DbValue[Boolean] {
    override def value(r: ResultSet, i: Int): Boolean =
      r.getBoolean(i)
  }
  implicit object BlobDbValue extends DbValue[Array[Byte]] {
    override def value(r: ResultSet, i: Int): Array[Byte] =
      r.getBytes(i)
  }
  implicit object IntDbValue extends DbValue[Int] {
    override def value(r: ResultSet, i: Int): Int =
      r.getInt(i)
  }
  implicit object LongDbValue extends DbValue[Long] {
    override def value(r: ResultSet, i: Int): Long =
      r.getLong(i)
  }
  implicit object StringDbValue extends DbValue[String] {
    override def value(r: ResultSet, i: Int): String =
      r.getString(i)
  }

}
