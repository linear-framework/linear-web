package com.linearframework.web.internal

import com.linearframework.web._

private[web] object CorsController extends Controller {

  OPTIONS("/*") { (request, response) =>
    val accessControlRequestHeaders: String = request.getHeader("Access-Control-Request-Headers").orNull
    if (accessControlRequestHeaders != null) {
      response.setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders)
    }

    val accessControlRequestMethod: String = request.getHeader("Access-Control-Request-Method").orNull
    if (accessControlRequestMethod != null) {
      response.setHeader("Access-Control-Allow-Methods", accessControlRequestMethod)
    }

    "OK"
  }

}
