package com.evrl.unclever

import javax.sql.DataSource

import org.scalatest.{Suite, BeforeAndAfterEach}

/**
 * Mix this in into tests that are integration-y and need the database
 * defined in DatabaseSetup
 */
trait TestDatasource extends Suite with BeforeAndAfterEach {
  var ds: DataSource = null

  override def beforeEach = {
    ds = DatabaseSetup.freshDatasource
  }
}
