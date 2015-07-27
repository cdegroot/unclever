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

  override def execute: DB[Int] = talkToDatabase(stmt =>
    stmt.executeUpdate())

  override def createStatement(conn: Connection) = {
    val stmt = conn.prepareStatement(sql)
    params.zipWithIndex.map { case (param, i) =>
      param.bindIn(stmt, i + 1)
    }
    stmt
  }
}
