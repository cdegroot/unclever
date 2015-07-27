package com.evrl.unclever


import java.sql.{Connection, ResultSet, Statement}

import scala.util.{Success, Failure}
import scala.util.control.NonFatal

/**
 * A Query implementation that uses strings to describe queries.
 */
class StringQuery[S <: Statement](sql: String) extends Query {
  override def withParams(params: ParamValue[_]*): Query =
    new ParameterizedStringQuery(sql, params)

  override def map[T](m: RowMapper[T]): DB[Seq[T]] = talkToDatabase { stmt =>
    val results = stmt.executeQuery(sql)
    var accum = new scala.collection.mutable.ArrayBuffer[T]
    while (results.next()) {
      accum += mapRow(m, results)
    }
    accum.toSeq
  }

  override def mapOne[T](m: RowMapper[T]): DB[Option[T]] = talkToDatabase { stmt =>
    val results = stmt.executeQuery(sql)
    if (results.next()) {
      Some(mapRow(m, results))
    } else {
      None
    }
  }

  override def execute: DB[Int] = talkToDatabase(stmt => stmt.executeUpdate(sql))

  protected def mapRow[T](m: RowMapper[T], results: ResultSet): T = {
    m(new ResultSetRow() {
      override def col[A](i: Int)(implicit ev: DbValue[A]): A =
        ev.value(results, i)
    })
  }

  protected def talkToDatabase[T](f: S => T): DB[T] = { conn =>
    // This BS is the reason we want to wrap JDBC interactions ;-)
    try {
      val stmt = createStatement(conn)
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

  protected def createStatement(conn: Connection): S = {
    conn.createStatement
  }
}
