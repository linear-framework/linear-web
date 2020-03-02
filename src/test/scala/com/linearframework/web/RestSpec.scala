package com.linearframework.web

import com.linearframework.BaseSpec
import javax.net.ssl.HttpsURLConnection
import java.io.InputStream
import java.net.{HttpURLConnection, URL}
import java.util
import scala.io.Source
import scala.jdk.CollectionConverters._

abstract class RestSpec extends BaseSpec {

  protected var server: Server = _
  protected val conf: Server.Configuration

  override protected def beforeAll(): Unit = {
    server = conf.start()
  }

  override protected def afterAll(): Unit = {
    server.stop()
  }

  protected case class RestResponse(body: String, headers: Map[String, String])

  HttpsURLConnection.setDefaultHostnameVerifier((hostname: String, _) => {
    hostname == "localhost"
  })

  protected def get(url: String, headers: Map[String, String] = Map()): RestResponse = {
    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(1000)
    connection.setReadTimeout(1000)
    connection.setRequestMethod("GET")

    headers.foreach { case (name, contents) => connection.setRequestProperty(name, contents) }

    var inputStream: InputStream = null
    var source: Source = null
    try {
      inputStream = connection.getInputStream
      source = Source.fromInputStream(inputStream)
      val body = source.getLines().mkString("\n")
      val headers = connection.getHeaderFields.asScala
        .filter(_._1 != null)
        .map { case (key: String, values: util.List[String]) => key -> values.get(0) }
        .toMap
        .asInstanceOf[Map[String, String]]

      RestResponse(body, headers)
    }
    finally {
      if (source != null) {
        source.close()
      }
      if (inputStream != null) {
        inputStream.close()
      }
    }
  }

}
