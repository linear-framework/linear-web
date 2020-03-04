package com.linearframework.web

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

package object internal {

  lazy val jsonMapper: ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.setSerializationInclusion(Include.NON_ABSENT)
    mapper
  }

  lazy val xmlMapper: XmlMapper = {
    val mapper = new XmlMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.setSerializationInclusion(Include.NON_ABSENT)
    mapper
  }

}
