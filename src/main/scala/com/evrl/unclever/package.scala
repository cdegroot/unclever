package com.evrl

import java.sql.{Connection, SQLException}
import javax.sql.DataSource

import scala.language.implicitConversions
import scala.util.Try

package object unclever {

  /**
   * A Query object. Typically created from a SQL string.
   */
  trait Query {
    def withParams(args: Any*): Query
    def in(conn: Connection): ConnectedQuery
    def tryIn(conn: Connection): ConnectedQueryTry
    def executeIn(conn: Connection): Unit
  }


  /**
   * A wrapper around java.sql.ResultSet that makes it nicer for Scala
   * people.
   */
  trait ResultSetRow {
    def col(i: Int): DbValue
  }

  /**
   * A value of a single column in a single row. Can be any type
   * supported by ResultSet.
   */
  trait DbValue

  /**
   * A query that has been assigned to a connection.
   */
  trait ConnectedQuery {
    def onError(handler: (SQLException) => Unit): ConnectedQuery = ???

    /**
     * Execute the query and map the result set using the
     * rowmapper.
     * @param m A function to extract a row from a java.sql.ResultSet
     * @tparam T The type of the result
     * @return A (possibly empty) list of results
     */
    def map[T](m: unclever.RowMapper[T]): Seq[T]
  }

  /**
   * A query that has been assigned to a connection and will employ Try.
   */
  trait ConnectedQueryTry {
    def map[T](m: unclever.RowMapper[T]): Try[Seq[T]]
  }

  /**
   * Connection wrapping
   */
  def withConnectionFrom[T](ds: DataSource)(f : Connection => T): Try[T] = ???

  /**
   * Type that describes how to map from a ResultSet to a T
   *
   * @tparam T
   */
  type RowMapper[T] = ResultSetRow => T

  // Bunch of default mappers
  implicit def dbValueToInt(v: DbValue): Int = ???
  implicit def dbValueToString(v: DbValue): String = ???

  // Scala doesn't do this for you, alas.
  implicit def optDbValueToInt(ov: Option[DbValue]): Option[Int] = ???

  /**
   * Support sql"..." syntax to create queries. Looks nice in IntelliJ because
   * it triggers syntax highlighting inside the string.
   */
  implicit class QueryHelper(private val sc: StringContext) extends AnyVal {
    def sql(args: Any*): Query = ???
  }
}
