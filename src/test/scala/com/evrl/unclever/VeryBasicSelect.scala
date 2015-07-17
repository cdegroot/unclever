package com.evrl.unclever

import java.sql.{SQLException, ResultSet, Statement, Connection}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, ShouldMatchers}

/**
 * Test the very basics of selecting data. This drives more implementation than you'd
 * imagine ;-)
 */
class VeryBasicSelect extends FlatSpec with ShouldMatchers with MockFactory {

  val query = sql"select 1".mapOne(_.col[Int](1))

  "a simple select" should "return correctly mapped results" in {
    val connection = mock[Connection]
    val statement = mock[Statement]
    val resultset = mock[ResultSet]

    (connection.createStatement _).expects().returning(statement)
    (statement.executeQuery _).expects("select 1").returning(resultset)
    (statement.close _).expects()
    (resultset.getInt (_: Int)).expects(1).returns(42)
    (resultset.close _).expects()

    val result = tryIn(connection)(query)
    result.isSuccess should be(true)
    val resultValue: Int = result.get
    resultValue should be(42)
  }

  it should "handle connection errors" in {
    val connection = mock[Connection]
    val exception = new SQLException()

    (connection.createStatement _).expects().throws(exception)
    (connection.rollback _).expects()

    expectFailure(connection, exception)
  }

  it should "handle statement errors" in {
    val connection = mock[Connection]
    val statement = mock[Statement]
    val exception = new SQLException()

    (connection.createStatement _).expects().returning(statement)
    (statement.executeQuery _).expects("select 1").throws(exception)
    (statement.close _).expects()
    // TODO - connection rollback?

    expectFailure(connection, exception)
  }

  it should "handle resultset errors" in {
    val connection = mock[Connection]
    val statement = mock[Statement]
    val resultset = mock[ResultSet]
    val exception = new SQLException()

    (connection.createStatement _).expects().returning(statement)
    (statement.executeQuery _).expects("select 1").returning(resultset)
    (statement.close _).expects()
    (resultset.getInt (_: Int)).expects(1).throws(exception)
    (resultset.close _).expects()

    expectFailure(connection, exception)
  }

  /** Expects the query to fail */
  def expectFailure(connection: Connection, exception: SQLException): Unit = {
    val result = tryIn(connection)(query)
    result.isFailure should be(true)
    the[SQLException] thrownBy result.get should be(exception)
  }

}
