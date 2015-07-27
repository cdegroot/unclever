package com.evrl.unclever

import javax.sql.DataSource

import scala.util.Random

/**
 * Setup an H2 database and return a datasource. Note that this code already
 * uses Unclever to talk to the database so just running main here is a partial
 * integration tet.
 */
object DatabaseSetup {

  /**
   * Get a datasource that represents a sparkling fresh database. Every
   * invocation of this returns a random database, so you can call this
   * during parallel testing as well.
   *
   * @return the datasource representing the database.
   */
  def freshDatasource: DataSource = {
    def makeDataSource: DataSource = {
      val ds = new org.h2.jdbcx.JdbcDataSource()
      ds.setUrl("jdbc:h2:mem:" + new Random().alphanumeric.take(32).mkString)
      ds
    }
    def setupDatabase(ds: DataSource) = {
      dropSchema(ds)
      createSchema(ds)
      fillDatabase(ds)
    }

    val ds = makeDataSource
    setupDatabase(ds)
    ds
  }



  case class Table(name: String, definition: String) {
    def drop = s"drop table if exists $name"
    def create = s"create table $name $definition"
  }

  case class RowData(tableName: String, values: Seq[String]) {
    def inserts = values.map(value => s"insert into $tableName values $value")
  }

  val tables = Seq(
    Table("emp", "(id INT NOT NULL, mgr INT NOT NULL, name VARCHAR(255))")
  )

  def rowDatas = Seq(
    RowData("emp", Seq(
      "(1, 1, 'Mr. President')",
      "(2, 1, 'Mr. CFO')",
      "(3, 2, 'Mr. Financial Director')",
      "(4, 3, 'Joe Beancounter')"
    ))
  )

  private def dropSchema(ds: DataSource): Unit =
    assert(tryWith(ds)(tables.map(_.drop)).isSuccess)

  private def createSchema(ds: DataSource): Unit =
    assert(tryWith(ds)(tables.map(_.create)).isSuccess)

  private def fillDatabase(ds: DataSource) = {
    val statements = for (r <- rowDatas; i <- r.inserts) yield i
    val result = tryWith(ds)(statements)
    assert(statements.length == result.get)
  }
  
  // for manual testing of the above
  def main(args: Array[String]) {
    freshDatasource
  }
}
