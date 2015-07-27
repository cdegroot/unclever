package com.evrl.unclever

import java.sql.{PreparedStatement, Connection, Statement}

import scala.util.{Failure, Success}
import scala.util.control.NonFatal

class ParameterizedStringQuery(sql: String, params: Seq[ParamValue[_]])
  extends StringQuery[PreparedStatement](sql) {

  override def withParams(moreParams: ParamValue[_]*): Query =
    new ParameterizedStringQuery(sql, params ++ moreParams)

  override def map[T](m: RowMapper[T]): DB[Seq[T]] = talkToDatabase { stmt =>
    val results = stmt.executeQuery()
    var accum = new scala.collection.mutable.ArrayBuffer[T]
    while (results.next()) {
      accum += mapRow(m, results)
    }
    accum.toSeq
  }

  override def mapOne[T](m: RowMapper[T]): DB[Option[T]] = talkToDatabase { stmt =>
    val results = stmt.executeQuery()
    if (results.next()) {
      Some(mapRow(m, results))
    } else {
      None
    }
  }
  override def execute: DB[Int] = talkToDatabase(stmt =>
    stmt.executeUpdate())

  override def createStatement(conn: Connection) = {
    val stmt = conn.prepareStatement(sql)
    params.zipWithIndex.foreach { case (param, i) =>
      param.bindIn(stmt, i + 1)
    }
    stmt
  }

  override def andGetKey[T: DbValue]: DB[T] = talkToDatabase { stmt =>
    // TODO check whether this is H2 specific. RETURN_GENERATED_KEYS is not available in PreparedStatement.
    stmt.executeUpdate()
    val results = stmt.getGeneratedKeys
    if (results.next()) {
      mapRow(_.col[T](1), results)
    } else {
      throw new RuntimeException(
        "Unexpected situation: insert succeeded but no primary key returned")
    }
  }
}
