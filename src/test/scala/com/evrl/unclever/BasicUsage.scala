package com.evrl.unclever

import java.sql.Connection
import javax.sql.DataSource

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, ShouldMatchers}

import scala.util.{Success, Failure}

class BasicUsage extends FlatSpec with ShouldMatchers with MockFactory {

  val connection = mock[Connection]

  "unclever" should "make querying nice and simple" ignore {
    val query = sql"select id from emp where emp = ?"

    val result: Option[Int] = query.withParams("1").in(connection).map(_.col(1)).headOption
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

    val results: Seq[Actor] = sql"select first_name, last_name"
      .in(connection).map(r => Actor(r.col(1), r.col(2)))
  }

  it should "be able to execute arbitrary statements" ignore {
    sql"create table emp(id int, mgr_id int, name)".executeIn(connection)
  }

  it should "support error handling" ignore {
    val badQuery = sql"select * from nonexistent_table"
    val result: Option[Int] = badQuery
      .in(connection).onError(e => throw new Exception(e)).map(_.col(1)).headOption

    // Or, using the default handling
    val nada: Option[_] = badQuery.in(connection).map(_.col(1)).headOption
    nada should be(None)

    // Or, using try
    val tryNada: Option[_] = badQuery.tryIn(connection).map(_.col(1)) match {
      case Failure(e) => println("E!"); None
      case Success(v) => Some(v)
    }
    tryNada should be(None)
  }

  it should "support safe operations" ignore {
    val dataSource = mock[DataSource]

    withConnectionFrom(dataSource) { c =>
      sql"select 1".in(c).map(_.col(1)).headOption should be(Some(1))
    }
  }
}
