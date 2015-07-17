package com.evrl.unclever

import java.sql.{ResultSet, Statement, Connection}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, ShouldMatchers}

/**
 * Test the very basics of selecting data. This drives more implementation than you'd
 * imagine ;-)
 */
class VeryBasicSelect extends FlatSpec with ShouldMatchers with MockFactory {

  "a simple select" should "return correctly mapped results" in {
    val connection = mock[Connection]
    val statement = mock[Statement]
    val resultset = mock[ResultSet]
    val query = sql"select 1".mapOne(_.col(1))

    (connection.createStatement _).expects().returning(statement)
    (statement.executeQuery _).expects("select 1").returning(resultset)
    (statement.close _).expects()
    (resultset.getInt (_: Int)).expects(1).returns(1)
    (resultset.close _).expects()

    val result = tryIn(connection)(query)
    result.isSuccess should be(true)
    val resultValue: Int = result.get
    resultValue should be(1)
  }
}
