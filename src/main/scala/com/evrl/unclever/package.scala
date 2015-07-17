package com.evrl

import java.sql.{ResultSet, Connection, SQLException}
import javax.sql.DataSource

import scala.language.implicitConversions
import scala.util.Try

/**
 * The unclever package object holds the API to unclever, calling out to
 * implementation methods for all actual work except for the very simplest
 * of operations.
 */
package object unclever {

  // Part one: connection/statement execution stuff

  /**
   * A database operation, returning a result of type A
   * @tparam A the return type of the database operation
   */
  type DB[A] = Connection => Try[A]

  /**
   * Try the database operation in a connection
   *
   * @param conn
   * @param op
   * @tparam A
   * @return
   */
  def tryIn[A](conn: Connection)(op: => DB[A]): Try[A] = op(conn)

  /**
   * Try the database operation in a fresh connection obtained
   * from the datasource
   */
  def tryWith[A](ds: DataSource)(op: => DB[A]): Try[A] = ???

  /**
   * A Query object. Typically created from a SQL string.
   */
  trait Query {
    def withParams(args: Any*): Query

    def map[T](m: RowMapper[T]): DB[Seq[T]]
    def mapOne[T](m: RowMapper[T]): DB[T]
  }

  /** So we can try direct queries without mapping */
  implicit def queryToDbOp(q: Query): DB[Unit] = ???

  // Part two: getting results back

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
  case class DbValue(resultSet: ResultSet, col: Int)

  /**
   * Type that describes how to map from a ResultSet to a T
   *
   * @tparam T
   */
  type RowMapper[T] = ResultSetRow => T

  // Bunch of default mappers
  implicit def dbValueToInt(v: DbValue): Int = v.resultSet.getInt(v.col)
  implicit def dbValueToString(v: DbValue): String = ???

  // Scala doesn't do this for you, alas.
  implicit def optDbValueToInt(ov: Option[DbValue]): Option[Int] = ???

  /**
   * Support sql"..." syntax to create queries. Looks nice in IntelliJ because
   * it triggers syntax highlighting inside the string.
   */
  implicit class QueryHelper(private val sc: StringContext) extends AnyVal {
    def sql(args: Any*): Query = new StringQuery(sc.parts.head)
  }
}
