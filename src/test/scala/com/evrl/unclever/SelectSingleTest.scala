package com.evrl.unclever

import javax.sql.DataSource

import org.scalatest._

/**
 * Where select statements that return a single value get exercised.
 */
class SelectSingleTest extends FlatSpec with ShouldMatchers with TestDatasource {

  "single select" should "return the correct result" in {
    val count = tryWith(ds)(sql"select count(*) from emp".as[Int])
    count.isSuccess should be(true)
    count.get.isDefined should be(true)
    count.get.get should be(4)
  }

  it should "return an error if the query is wrong" in {
    val count = tryWith(ds)(sql"select YO! BRO! I'M A SYNTAX ERROR!'".as[Int])
    count.isSuccess should be(false)
    count.failed.get shouldBe an [org.h2.jdbc.JdbcSQLException]
  }
}
