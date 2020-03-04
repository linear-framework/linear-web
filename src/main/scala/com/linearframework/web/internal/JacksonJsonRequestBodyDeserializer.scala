package com.linearframework.web.internal

import com.linearframework.web.RequestBodyDeserializer
import scala.reflect.ClassTag

private[web] object JacksonJsonRequestBodyDeserializer extends RequestBodyDeserializer {

  override def apply[T <: Product with Serializable](body: String)(implicit classTag: ClassTag[T]): T = {
    val clazz = classTag.runtimeClass.asInstanceOf[Class[T]]
    jsonMapper.readValue(body, clazz)
  }

}
