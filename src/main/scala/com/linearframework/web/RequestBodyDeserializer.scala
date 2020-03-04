package com.linearframework.web

import scala.reflect.ClassTag

/**
 * Handles conversion of a Request Body into a case class instance
 */
trait RequestBodyDeserializer {

  /**
   * Converts the given request body into a case class instance
   */
  def apply[T <: Product with Serializable](body: String)(implicit classTag: ClassTag[T]): T

}
