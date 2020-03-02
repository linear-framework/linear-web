package com.linearframework.web.serverConfig

import com.linearframework.web.{RestSpec, _}

class RegistrySpec extends RestSpec {

  override protected val conf: Server.Configuration =
    Server.autoScan("com.linearframework.hello")
      .register[String]("hello")
      .register[String]("world")

  "Registered objects" should "be accessible to all server components" in {
    get("http://localhost:4567/registered/all").body should be ("hello, world")
    get("http://localhost:4567/registered/one").body should be ("hello")
  }
}
