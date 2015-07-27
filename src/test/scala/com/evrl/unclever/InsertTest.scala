package com.evrl.unclever

import org.scalatest._

class InsertTest extends FlatSpec with ShouldMatchers with TestDatasource {

  "inserting" should "make the primary key available" in {
    val result = tryWith(ds)(
      sql"insert into emp(mgr, name) values (1, 'John Doe')".andGetKey[Int])
    result.isSuccess should be(true)
    result.get should be(5)
  }

  it should "work with parameters" ignore {
    val result = tryWith(ds)(
    sql"insert into emp(mgr, name) values(?, ?)"
      .withParams(1, "John Doe")
      .andGetKey[Int]
    )
    result.isSuccess should be(true)
    result.get should be(5)
  }

  it should "return a sequence of primary keys on multiple inserts" ignore {
    // TODO something like seqStringToDbOp is planned here
  }


}
