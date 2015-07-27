package com.evrl.unclever

import java.sql.{PreparedStatement, Connection, Statement}

import scala.util.{Failure, Success}
import scala.util.control.NonFatal

class ParameterizedStringQuery(sql: String, params: Seq[ParamValue[_]])
  extends StringQuery[PreparedStatement](sql) {

  /**
   * Because it was so simple to implement, you can chain withParams calls. What
   * you do with it and how to protect your sanity while doing it, is not my
   * problem.
   * @param moreParams
   * @return
   */
  override def withParams(moreParams: ParamValue[_]*): Query =
    new ParameterizedStringQuery(sql, params ++ moreParams)

  protected override def executeQueryInStmt(statement: PreparedStatement) =
    statement.executeQuery()

  protected override def executeUpdateInStmt(statement: PreparedStatement) =
    statement.executeUpdate()

  override def createStatement(conn: Connection) = {
    val stmt = conn.prepareStatement(sql)
    params.zipWithIndex.foreach { case (param, i) =>
      param.bindIn(stmt, i + 1)
    }
    stmt
  }

  protected override def executeForInsertInStmt(statement: PreparedStatement) =
    // TODO check whether this is H2 specific. RETURN_GENERATED_KEYS is not available in PreparedStatement.
    statement.executeUpdate()
}
