package com.evrl.unclever

import java.sql.ResultSet
import scala.language.implicitConversions

/** A type class for database values */
trait DbValue[A] {
  def value(r: ResultSet, i: Int): A
}

/**
 * All default instances of the DbValue type class.
 *
 * Note that this is an opiniated set of types. I think it constitutes
 * all the types you should use. My ideas on what got left out:
 * - Date/time: don't go there. Unix timestamps (or their 64 bit equivalent,
 *   but keep the database stupid and do TZ conversions as late as possible,
 *   UTC is way easier to calculate with);
 * - Floats: I'm not a scientist. Could be added, as long as you don't promise
 *   to use it for money;
 * - BigDecimal: I prefer storing money values in longs (in the smallest denomination
 *   of a currency, e.g. dollar cents for dollars). Again, keep the database
 *   stupid. Also, long operations are faster;
 * - Clobs: Let the application do the charset conversion, so store blobs;
 * - All the streaming blob support: if your blobs are that big, you shouldn't be
 *   storing them in an RDBMS. Store a pointer to S3 or something;
 * - All the XML, Nchar, Ref, ...: see "keep the database stupid". Frankly, I only
 *   ever used that stuff when I wrote JDBC drivers and thus had to.
 *
 * Having said that, one of the reasons that I wrote this down as a type class is
 * that you can ignore me and extend as you like :)
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
