package com.linearframework.json

import com.google.gson.GsonBuilder
import com.linearframework.web._
import java.util
import scala.beans.BeanProperty

case class Person(@BeanProperty name: String, @BeanProperty age: Int)

object JsonController extends Controller {
  private val gson = new GsonBuilder().create()
  private implicit val gsonTransformer: ResponseTransformer = { (result, response) =>
    response.setContentType(JSON)
    gson.toJson(result)
  }

  GET("/people") { (_, _) =>
    val list = new util.ArrayList[Person]
    list.add(Person("Steve", 19))
    list.add(Person("Billy", 23))
    list
  }
}
