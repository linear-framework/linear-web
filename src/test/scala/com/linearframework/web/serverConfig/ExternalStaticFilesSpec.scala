package com.linearframework.web.serverConfig

import com.linearframework.web._
import java.io.File

class ExternalStaticFilesSpec extends RestSpec {
  private val path = {
    val page = this.getClass.getClassLoader.getResource("public/index.html")
    val file = new File(page.toString.replaceAll("file:", ""))
    file.getAbsolutePath.replaceAll("index.html", "")
  }

  override protected val conf: Server.Configuration =
    Server
      .autoScan("com.linearframework.hello")
      .staticFiles(EXTERNAL, path)

  "Static file hosting" should "be supported with external resources" in {
    val response = get("http://localhost:4567/index.html")
    response.body should be ("""
      |<!DOCTYPE html>
      |<html lang="en">
      |<head>
      |  <meta charset="UTF-8">
      |  <title>Title</title>
      |</head>
      |<body>
      |  Body
      |</body>
      |</html>
    """.stripMargin.trim)
  }
}
