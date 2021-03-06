package com.codingkapoor.pageselectionbykeyword.selector

import com.codingkapoor.pageselectionbykeyword.model.Page
import com.codingkapoor.pageselectionbykeyword.model.Query

import com.codingkapoor.pageselectionbykeyword.util.SimpleUserInputFileReader

trait SimplePageSelector extends PageSelector with SimpleUserInputFileReader {

  override def pageSelectionByKeyword(fileName: Option[String]) = {

    val (queries, pages) = readUserInputFile(fileName)
    
    def strength(y: Query, x: Page) = {
      val r = for { (i, j) <- x.keywords; (p, q) <- y.keywords } yield {
        if (i == p) j * q else 0
      }

      r.sum
    }

    val i: List[(Int, List[Int])] = for { query <- queries } yield {
      val ls = for { page <- pages } yield {
        (page.id, strength(query, page))
      }

      (query.id, ls sortWith { _._2 > _._2 } take (maxPageSelections) filter { _._2 != 0 } map { _._1 })
    }

    for { r <- i } yield {
      Query.identifier + r._1 + ":" + (r._2 foldLeft ("")) { (acc, x) => acc + " " + Page.identifier + x }
    }
  }

}
