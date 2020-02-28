package com.linearframework.app

import com.linearframework.web._

object App extends Controller with Filter {
  var afterFilterCount = 0
  var afterAfterFilterCount = 0
  var getOpenCount = 0
  var getSecureCount = 0

  GET("/open/hello") { (_, _) =>
    getOpenCount = getOpenCount + 1
    "hello"
  }

  GET("/secure/hello") { (_, _) =>
    getSecureCount = getSecureCount + 1
    "hello"
  }

  BEFORE("/secure/*") { (request, _) =>
    val isAuthorized = request.getHeader("Authorization").getOrElse("") == "let me in"
    if (!isAuthorized) {
      throw new IllegalAccessException("You're not allowed to be here!")
    }
  }

  AFTER { (_, _) =>
    afterFilterCount = afterFilterCount + 1
  }

  AFTER_AFTER { (_, _) =>
    afterAfterFilterCount = afterAfterFilterCount + 1
  }

}

object IllegalAccessHandler extends ExceptionHandler[IllegalAccessException] {
  override val exceptionClass: Class[IllegalAccessException] = classOf[IllegalAccessException]

  override def apply(exception: IllegalAccessException, request: Request, response: Response): Unit = {
    response.setStatus(UNAUTHORIZED)
    response.setBody(exception.getMessage)
  }
}

object FourZeroFour extends NotFoundHandler {
  override def apply(request: Request, response: Response): Unit = {
    response.setStatus(NOT_FOUND)
    response.setBody("What exactly are you looking for?")
  }
}

