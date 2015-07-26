package com.evrl.unclever

import javax.sql.DataSource

/**
 * Setup an H2 database and return a datasource. Note that this code already
 * uses Unclever to talk to the database so just running main here is a partial
 * integration tet.
 */
object DatabaseSetup {

  def before: DataSource = {
    setupDatabase
    datasource
  }

  private lazy val datasource: DataSource = {
    val ds = new org.h2.jdbcx.JdbcDataSource()
    ds.setUrl("jdbc:h2:mem:unclever")
    ds
  }

  private def setupDatabase = {
    dropSchema
    createSchema
    fillDatabase
  }

  case class Table(name: String, definition: String) {
    def drop = s"drop table if exists $name"
    def create = s"create table $name $definition"
  }

  val tables = Seq(
    Table("emp", "(id INT NOT NULL, mgr INT NOT NULL, name VARCHAR(255))")
  )

  private def dropSchema: Unit =
    println("drops: " + tryWith(datasource)(tables.map(_.drop)))

  private def createSchema: Unit =
    println("creates: " + tryWith(datasource)(tables.map(_.create)))

  case class RowData(tableName: String, values: Seq[String]) {
    def inserts = values.map(value => s"insert into $tableName values $value")
  }

  def rowDatas = Seq(
    RowData("emp", Seq(
      "(1, 1, 'Mr. President')",
      "(2, 1, 'Mr. CFO')",
      "(3, 2, 'Mr. Financial Director')",
      "(4, 3, 'Joe Beancounter')"
    ))
  )

  private def fillDatabase = {
    val statements = for (r <- rowDatas; i <- r.inserts) yield i
    val result = tryWith(datasource)(statements)
    println(s"got $result from ${statements.length} statements")
  }


  // for manual testing of the above
  def main(args: Array[String]) {
    setupDatabase
  }
}
