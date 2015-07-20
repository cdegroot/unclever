package com.evrl.unclever

import scala.util.{Success, Failure}
import scala.util.control.NonFatal

/**
 * A Query implementation that uses strings to describe queries.
 */
class StringQuery(sql: String) extends Query {
  override def withParams(args: Any*): Query = ???

  override def map[T](m: RowMapper[T]): DB[Seq[T]] = ???

  override def mapOne[T](m: RowMapper[T]): DB[Option[T]] = { conn =>
    // This BS is the reason we want to wrap JDBC interactions ;-)
    try {
      val stmt = conn.createStatement
      try {
        val results = stmt.executeQuery(sql)
        try {
          if (results.next()) {
            Success(Some(m(new ResultSetRow() {
              override def col[A](i: Int)(implicit ev: DbValue[A]): A =
                ev.value(results, i)
            }
            )))
          } else {
            Success(None)
          }
        } catch {
          case NonFatal(e) => Failure(e)
        } finally {
          results.close()
        }
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
