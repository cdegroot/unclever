package com.evrl.unclever

import org.scalatest._

/**
 * Where we do funky stuff with question marks
 */
class SelectWithParametersTest extends FlatSpec with ShouldMatchers with TestDatasource{

  "parameterized select" should "return the correct result" in {
    val query = sql"select id from emp where mgr = ?".withParams(1)
    val count = tryWith(ds)(query.map(_.col[Int](1)))
    print(s"result = $count")
    count.get
    count.isSuccess should be(true)
    count.get should be(Seq(1,2))
  }
}
