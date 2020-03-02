package com.linearframework.web

import com.linearframework.app.App
import java.io.IOException

class ComponentsTest extends RestSpec {
  override protected val conf: Server.Configuration = Server.autoScan("com.linearframework.app").logRequests(true)

  "Before filters and exception handlers" should "apply" in {
    App.getSecureCount should be (0)
    val e = intercept[IOException] {
      get("http://localhost:4567/secure/hello")
    }

    e.getMessage.contains("response code: 401") should be (true)
    App.getSecureCount should be (0)

    get("http://localhost:4567/secure/hello", Map("Authorization" -> "let me in")).body should be ("hello")
    App.getSecureCount should be (1)
  }

  "After filters" should "apply" in {
    App.getOpenCount should be (0)
    val c1 = App.afterFilterCount
    val c2 = App.afterAfterFilterCount

    get("http://localhost:4567/open/hello")

    App.afterFilterCount > c1 should be (true)
    App.afterAfterFilterCount > c2 should be (true)
    App.getOpenCount should be (1)
  }
}
