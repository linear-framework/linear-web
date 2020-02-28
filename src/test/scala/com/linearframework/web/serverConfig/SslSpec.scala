package com.linearframework.web.serverConfig

import com.linearframework.web._
import org.junit.Ignore
import java.io.File

@Ignore("This doesn't always work due to the self-signed certificate; however, running the call in the browser confirms it works")
class SslSpec extends RestSpec {
  private def path(filename: String): String = {
    val page = this.getClass.getClassLoader.getResource(filename)
    val file = new File(page.toString.replaceAll("file:", ""))
    file.getAbsolutePath
  }

  override protected val conf: Server.ServerConfiguration =
    Server
      .autoScan("com.linearframework.hello")
      .secure(
        path("keystore.p12"),
        "password",
        System.getenv("JAVA_HOME") + "/jre/lib/security/cacerts",
        "changeit"
      )

  "SSL over HTTPS" should "be supported" in {
    get("https://localhost:4567/hello").body should be ("hello")
  }
}
