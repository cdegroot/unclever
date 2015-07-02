package com.evrl.unclever

import java.sql.{Connection, ResultSet}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ShouldMatchers, FlatSpec}

class BasicUsage extends FlatSpec with ShouldMatchers with MockFactory {

  "unclever" should "make querying nice and simple" in {

    val query = "select id from emp where emp = 1"
    val mapper: RowMapper[Int] = r => r.getInt(1)
    val connection = mock[Connection]

    val result: Option[Int] = query.in(connection).map(mapper).headOption
  }
}
