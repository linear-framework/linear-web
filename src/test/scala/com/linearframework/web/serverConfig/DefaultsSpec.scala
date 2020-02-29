package com.linearframework.web.serverConfig

import com.linearframework.web.{RestSpec, Server}
import java.io.{FileNotFoundException, IOException}

class DefaultsSpec extends RestSpec {

  override protected val conf: Server.ServerConfiguration =
    Server.autoScan("com.linearframework.hello")

  "Default server configuration" should "be applicable to simple use cases" in {
    val restResponse = get("http://localhost:4567/hello")
    restResponse.body should be("hello")
    restResponse.headers.get("Access-Control-Allow-Origin") should be(None)

    a[FileNotFoundException] should be thrownBy {
      get("http://localhost:4567/index.html")
    }

    val e = intercept[IOException] {
      get("http://localhost:4567/error")
    }
    e.getMessage.contains("Server returned HTTP response code: 500") should be(true)

    get("http://localhost:4567/registered/all").body should be("")
    get("http://localhost:4567/registered/one").body should be("")
  }

  "Response Transformers" can "re-write endpoint results" in {
    val response = get("http://localhost:4567/hola")
    response.body should be ("hola")
  }
}
