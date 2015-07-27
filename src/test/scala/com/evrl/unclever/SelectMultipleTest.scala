package com.evrl.unclever

import org.scalatest._

/**
 * Where select statements that return multiple values get exercised
 */
class SelectMultipleTest extends FlatSpec with ShouldMatchers with TestDatasource {

  "multiple select" should "return the correct result" in {
    val count = tryWith(ds)(sql"select id from emp".map(_.col[Int](1)))
    count.isSuccess should be(true)
    count.get should be(Seq(1,2,3,4))
  }

  it should "return an error for bad queries" in {
    val count = tryWith(ds)(sql"select id  emp".map(_.col[Int](1)))
    count.isSuccess should be(false)
    count.failed.get shouldBe an [org.h2.jdbc.JdbcSQLException]
  }
}
