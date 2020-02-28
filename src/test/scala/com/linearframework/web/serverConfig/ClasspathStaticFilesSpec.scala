package com.linearframework.web.serverConfig

import com.linearframework.web._

class ClasspathStaticFilesSpec extends RestSpec {
  override protected val conf: Server.ServerConfiguration =
    Server
      .autoScan("hello")
      .staticFiles(CLASSPATH, "public/")

  "Static file hosting" should "be supported with classpath resources" in {
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
