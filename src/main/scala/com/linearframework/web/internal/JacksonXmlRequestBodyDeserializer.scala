package com.linearframework.web.internal

import com.linearframework.web.RequestBodyDeserializer
import scala.reflect.ClassTag

private[web] object JacksonXmlRequestBodyDeserializer extends RequestBodyDeserializer {

  override def apply[T <: Product with Serializable](body: String)(implicit classTag: ClassTag[T]): T = {
    val clazz = classTag.runtimeClass.asInstanceOf[Class[T]]
    xmlMapper.readValue(body, clazz)
  }

}
