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
   * Try the database operation in a connection. Feel free to ignore this
   * method and use op(conn) directly if you think it's more readable.
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

    /**
     * Map a query to a sequence of values. This will, when run,
     * result in a Try[Seq[T]] with not found mapped to an empty
     * sequence
     * @param m the rowmapper to use
     * @tparam T the result type
     * @return a DB operation representing the map
     */
    def map[T](m: RowMapper[T]): DB[Seq[T]]

    /**
     * Map a query to a single value. This will, when run, result
     * in a Try[Option[T]] with not found mapped to a None.
     * @param m the rowmapper to use
     * @tparam T the result type
     * @return a DB operation representing the map
     */
    def mapOne[T](m: RowMapper[T]): DB[Option[T]]
  }

  /** So we can try direct queries without mapping */
  implicit def queryToDbOp(q: Query): DB[Unit] = ???

  // Part two: getting results back

  /** A typeclass for database values */
  trait DbValue[A] {
    def value(r: ResultSet, i: Int): A
  }

  /** Make Int a member of DbValue */
  implicit object IntDbValue extends DbValue[Int] {
    def value(r: ResultSet, i: Int): Int = r.getInt(i)
  }

  /** Make String a member of DbValue */
  implicit object StringDbValue extends DbValue[String] {
    def value(r: ResultSet, i: Int): String = r.getString(i)
  }

  /**
   * A wrapper around java.sql.ResultSet that makes it nicer for Scala
   * people.
   */
  trait ResultSetRow {
    def col[A: DbValue](i: Int): A
  }

  /**
   * Type that describes how to map from a ResultSet to a T
   *
   * @tparam T
   */
  type RowMapper[T] = ResultSetRow => T


  /**
   * Support sql"..." syntax to create queries. Looks nice in IntelliJ because
   * it triggers syntax highlighting inside the string.
   */
  implicit class QueryHelper(private val sc: StringContext) extends AnyVal {
    def sql(args: Any*): Query = new StringQuery(sc.parts.head)
  }
}
