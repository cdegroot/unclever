package com.evrl.unclever


import java.sql.Statement

import scala.util.{Success, Failure}
import scala.util.control.NonFatal

/**
 * A Query implementation that uses strings to describe queries.
 */
class StringQuery(sql: String) extends Query {
  override def withParams(args: Any*): Query = ???

  override def map[T](m: RowMapper[T]): DB[Seq[T]] = ???

  override def mapOne[T](m: RowMapper[T]): DB[Option[T]] = talkToDatabase { stmt =>
    val results = stmt.executeQuery(sql)
    if (results.next()) {
      Some(m(new ResultSetRow() {
        override def col[A](i: Int)(implicit ev: DbValue[A]): A =
          ev.value(results, i)
      }))
    } else {
      None
    }
  }


  override def execute: DB[Int] = talkToDatabase(stmt => stmt.executeUpdate(sql))

  private def talkToDatabase[T](f: Statement => T): DB[T] = { conn =>
    // This BS is the reason we want to wrap JDBC interactions ;-)
    try {
      val stmt = conn.createStatement
      try {
        Success(f(stmt))
      } catch {
        case NonFatal(e) => Failure(e)
      } finally {
        stmt.close()
      }
    } catch {
      case NonFatal(e) =>
        conn.rollback()
        Failure(e)
    }
  }
}
