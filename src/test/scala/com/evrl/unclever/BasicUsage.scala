package com.evrl.unclever

import java.sql.Connection
import javax.sql.DataSource

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, ShouldMatchers}

import scala.util.Try

/**
 * The tests in this class are all ignored by design. They serve as compilable documentation
 * on how to use the library. Compiler-driven-design: if you want to extend the library,
 * write an ignored test, make it compile by adding unimplemented methods, and tweak until
 * you like the code here. When done, write tests that exercise the new unimplemented
 * methods.
 */
class BasicUsage extends FlatSpec with ShouldMatchers with MockFactory {

  val connection = mock[Connection]

  it should "make querying nice and simple in a functional manner" ignore {
    val query = sql"select id from emp where emp = ?"

    val op: DB[Int] = query.withParams("1").mapOne(_.col(1))

    val result: Try[Int] = tryIn(connection)(op)
  }

  it should "be more scala-like than JDBC Template" ignore {
    // Copying from
    // http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html#jdbc-JdbcTemplate-examples-query
    //
    //    List<Actor> actors = this.jdbcTemplate.query(
    //      "select first_name, last_name from t_actor",
    //      new RowMapper<Actor>() {
    //        public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
    //          Actor actor = new Actor();
    //          actor.setFirstName(rs.getString("first_name"));
    //          actor.setLastName(rs.getString("last_name"));
    //          return actor;
    //        }
    //      });
    //

    case class Actor(first: String, last: String)

    // I think this is more concise ;-). By the way, in a past life I have done
    // performance testing, and saw - at least with the MySQL JDBC Driver - a significant
    // difference between fetch-by-name and fetch-by-column-index. I prefer the fast version.

    val results: Try[Seq[Actor]] = tryIn(connection) {
      sql"select first_name, last_name".map(r => Actor(r.col(1), r.col(2)))
    }
  }

  it should "be able to execute arbitrary statements" ignore {
    tryIn(connection)(sql"create table emp(id int, mgr_id int, name)")
  }

  it should "support safe operations" ignore {
    val dataSource = mock[DataSource]

    val result = tryWith(dataSource) {
      sql"select 1".mapOne(_.col(1))
    }
    result.isSuccess should be(true)
    result.get should be(1)
  }
}
