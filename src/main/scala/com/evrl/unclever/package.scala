package com.evrl

import java.sql.{Connection, ResultSet}

trait Query {
  def in(conn: Connection): ConnectedQuery
}

trait ConnectedQuery {
  def map[T](m: unclever.RowMapper[T]): List[T]
}

class QueryImpl(sql: String) extends Query {
  override def in(conn: Connection): ConnectedQuery = ???
}

package object unclever {

  type RowMapper[T] = ResultSet => T

  implicit def stringToQuery(s: String): Query = new QueryImpl(s)
}
