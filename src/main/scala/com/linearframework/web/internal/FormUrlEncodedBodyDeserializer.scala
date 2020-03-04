package com.linearframework.web.internal

import com.linearframework.web.RequestBodyDeserializer
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import scala.reflect.ClassTag
import scala.util.Try

private[web] object FormUrlEncodedBodyDeserializer extends RequestBodyDeserializer {

  override def apply[T <: Product with Serializable](body: String)(implicit classTag: ClassTag[T]): T = {
    val clazz = classTag.runtimeClass.asInstanceOf[Class[T]]

    val map =
      body.split("&")
        .map { pair =>
          val parts = pair.split("=")
          val left = URLDecoder.decode(Try(Option(parts(0)).getOrElse("")).getOrElse(""), StandardCharsets.UTF_8.toString)
          val right = URLDecoder.decode(Try(Option(parts(1)).getOrElse("")).getOrElse(""), StandardCharsets.UTF_8.toString)
          left -> right
        }
        .toMap

    jsonMapper.convertValue(map, clazz)
  }

}
