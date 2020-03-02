package com.linearframework.web.serverConfig

import com.linearframework.web.{RestSpec, Server}
import java.net.SocketTimeoutException

class ConfigurePortSpec extends RestSpec {

  override protected val conf: Server.Configuration =
    Server
      .autoScan("com.linearframework.hello")
      .port(5678)

  "Server port" should "be customizable" in {
    a [SocketTimeoutException] should be thrownBy {
      get("http://localhost:4567/hello")
    }

    get("http://localhost:5678/hello").body should be ("hello")
  }

}
