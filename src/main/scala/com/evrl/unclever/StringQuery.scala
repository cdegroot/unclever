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
    val results = executeQueryInStmt(stmt)
    var accum = new scala.collection.mutable.ArrayBuffer[T]
    while (results.next()) {
      accum += mapRow(m, results)
    }
    accum.toSeq
  }

  override def mapOne[T](m: RowMapper[T]): DB[Option[T]] = talkToDatabase { stmt =>
    val results = executeQueryInStmt(stmt)
    if (results.next()) {
      Some(mapRow(m, results))
    } else {
      None
    }
  }

  override def execute: DB[Int] = talkToDatabase(stmt =>
    executeUpdateInStmt(stmt))

  override def andGetKey[T: DbValue]: DB[T] = talkToDatabase { stmt =>
    executeForInsertInStmt(stmt)
    val results = stmt.getGeneratedKeys
    if (results.next()) {
      mapRow(_.col[T](1), results)
    } else {
      throw new RuntimeException(
        "Unexpected situation: insert succeeded but no primary key returned")
    }
  }

  // Map a single row from a result set
  private def mapRow[T](m: RowMapper[T], results: ResultSet): T = {
    m(new ResultSetRow() {
      override def col[A](i: Int)(implicit ev: DbValue[A]): A =
        ev.value(results, i)
    })
  }

  // Talk to the hand. Does most of the required exception catching.
  // This BS is the reason we want to wrap JDBC interactions ;-)
  private def talkToDatabase[T](f: S => T): DB[T] = asDB { conn =>
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

  // Down here is a bunch of hierarchy-specific methods that are
  // overridden by ParameterizedStringQuery to have the code above
  // work with PreparedStatements.

  protected def createStatement(conn: Connection): S =
    // TODO find out how to make this work without the asInstanceOf
    // (preferably without introducing a lot of crap)
    conn.createStatement.asInstanceOf[S]

  protected def executeQueryInStmt(statement: S) =
    statement.executeQuery(sql)

  protected def executeUpdateInStmt(statement: S) =
    statement.executeUpdate(sql)

  protected def executeForInsertInStmt(statement: S) =
    statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS)

}
