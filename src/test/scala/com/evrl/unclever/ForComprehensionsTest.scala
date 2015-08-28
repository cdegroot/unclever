package com.evrl.unclever

import org.scalatest._

import scala.util.Try

class ForComprehensionsTest extends FlatSpec with ShouldMatchers with TestDatasource {

  "unclever" should "support for comprehensions" in {

    def transMogrify(o: Option[String]): String = o.getOrElse("John Doe") + " Supreme"

    val prog: DB[Int] = for {
      currentName <- sql"select name from emp where id = ?".withParams(1).as[String]
      newName = transMogrify(currentName)
      result <- sql"update emp set name = ? where id = ?".withParams(newName, 1).execute
    } yield result

    val r: Try[Int] = prog(ds.getConnection)
    r.isSuccess should be(true)
    r.get should be(1)

    // check that we have the correct name
    val newName: Try[Option[String]] = tryWith(ds) {
      sql"select name from emp where id = ?".withParams(1).as[String]
    }
    newName.isSuccess should be(true)
    newName.get.get should be("Mr. President Supreme")
  }

  it should "support filters" in {
    val sql = sql"select name from emp where id = 42"
    val prog: DB[Option[String]] = for {
      // a bit of a contrived example, but it invokes withFilter...
      current <- sql.as[String] if current.isDefined
    } yield current

    val result: Try[Option[String]] = prog(ds.getConnection)
    result.isSuccess should be(false)
    val error = result.failed.get
    error shouldBe a[NoSuchElementException]
    error.getMessage should be("Predicate does not hold for None")
  }
}
