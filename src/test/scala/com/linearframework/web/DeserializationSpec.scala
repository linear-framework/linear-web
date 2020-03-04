package com.linearframework.web

import com.linearframework.web.internal.JacksonJsonRequestBodyDeserializer

class DeserializationSpec extends RestSpec {
  override protected val conf: Server.Configuration =
    Server
      .autoScan("com.linearframework.hello")
      .setDeserializer(CustomContentType("application", "linearjson"), JacksonJsonRequestBodyDeserializer)

  "JSON deserialization" should "be automatically supported" in {
    val body = """{"name": "Billy", "age": 28}"""
    val headers = Map("Content-Type" -> "application/json")
    val response = put("http://localhost:4567/person", body, headers)

    response.body should be ("Person(Billy,28)")
  }

  "URL Encoded Form deserialization" should "be automatically supported" in {
    val body = "name=Billy&age=28"
    val headers = Map("Content-Type" -> "application/x-www-form-urlencoded; charset=utf-8")
    val response = put("http://localhost:4567/person", body, headers)

    response.body should be ("Person(Billy,28)")
  }

  "XML deserialization" should "be automatically supported" in {
    val body = """
      <Person>
        <name>Billy</name>
        <age>28</age>
      </Person>
    """
    val headers = Map("Content-Type" -> "text/xml")
    val response = put("http://localhost:4567/person", body, headers)

    response.body should be ("Person(Billy,28)")
  }

  "Custom deserialization" should "be supported" in {
    val body = """{"name": "Billy", "age": 28}"""
    val headers = Map("Content-Type" -> "application/linearjson; charset=utf-8")
    val response = put("http://localhost:4567/person", body, headers)

    response.body should be ("Person(Billy,28)")
  }

  "Deserialization" should "not be supported if the Content-Type header is missing" in {
    val body = """{"name": "Billy", "age": 28}"""
    val response = put("http://localhost:4567/person", body)

    response.body should be ("Request is missing Content-Type header; cannot deserialize to object.")
  }

  it should "not be supported if the Content-Type doesn't have an associated deserializer" in {
    val body = """{"name": "Billy", "age": 28}"""
    val headers = Map("Content-Type" -> "application/customjson; charset=utf-8")
    val response = put("http://localhost:4567/person", body, headers)

    response.body should be ("No deserializer is available for content type [application/customjson]")
  }

}
