package com.linearframework.web.internal

import com.linearframework.web._

private[web] class CorsFilter(allowOrigin: String) extends Filter {
  BEFORE { (_, response) =>
    response.setHeader("Access-Control-Allow-Origin", allowOrigin)
    response.setHeader("Access-Control-Allow-Credentials", "true")
  }
}

