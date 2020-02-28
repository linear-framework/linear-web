package com.linearframework.web

import com.linearframework.web.internal.ServerRegistrant
import spark.Spark

/**
 * A filter specifically designed for handling a single type of exception.
 * In order to be automatically registered with the server,
 * exception handlers must be top-level Scala objects and cannot be nested/internal objects, classes, etc.
 */
trait ExceptionHandler[T <: Exception] extends ServerRegistrant {

  /**
   * The upper-bound type of exception that this filter should catch
   */
  val exceptionClass: Class[T]

  /**
   * Handles an uncaught exception in the filter chain
   */
  def apply(exception: T, request: Request, response: Response): Unit

  abstract override private[web] def register(): Unit = {
    Spark.exception(exceptionClass, handler)
  }

  private lazy val handler: spark.ExceptionHandler[T] = {
    (exception: T, request: spark.Request, response: spark.Response) => {
      val req = new RequestImpl(request)
      val res = new ResponseImpl(response)
      apply(exception, req, res)
    }
  }

}

