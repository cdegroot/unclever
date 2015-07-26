package com.evrl.unclever

import javax.sql.DataSource

import org.scalatest._

class SelectSingleTest extends FlatSpec with ShouldMatchers with BeforeAndAfter {
  var ds: DataSource = null

  before {
    ds = DatabaseSetup.before
  }

  "single select" should "return the correct result" in {
    val count = tryWith(ds)(sql"select count(*) from emp".mapOne(_.col[Int](1)))
    count.isSuccess should be(true)
    count.get.isDefined should be(true)
    count.get.get should be(4)
  }
}
