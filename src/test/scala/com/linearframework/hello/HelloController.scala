package com.linearframework.hello

import com.linearframework.web._
import scala.util.Try

case class ErrorRequestBody(name: String, age: Int)

object HelloController extends Controller {

  GET("/hello") { (_, _) =>
    "hello"
  }

  GET("/hola") { (_, _) =>
    "hello"
  } { (_, _) =>
    "hola"
  }

  GET("/error") { (_, _) =>
    throw new RuntimeException("Oops!")
  }

  POST("/errors/:errorId") { (_, _) =>
    throw new RuntimeException("Oops!")
  }

  PUT("/errors/:errorId/update") { (_, _) =>
    throw new RuntimeException("Oops!")
  }

  GET("/registered/all") { (_, _) =>
    all[String].mkString(", ")
  }

  GET("/registered/one") { (_, _) =>
    Try(the[String]).getOrElse("")
  }

}
