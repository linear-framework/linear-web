package com.linearframework.web.serverConfig

import com.linearframework.web.{RestSpec, Server}
import com.linearframework.web.Server._

class ConfigureCorsSpec extends RestSpec {

  override protected val conf: ServerConfiguration =
    Server
      .autoScan("com.linearframework.hello")
      .corsOrigin("my-origin")

  "CORS" should "be supported" in {
    val response = get("http://localhost:4567/hello")
    response.headers("Access-Control-Allow-Origin") should be ("my-origin")
  }

}
