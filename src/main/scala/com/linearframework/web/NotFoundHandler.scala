package com.linearframework.web

import com.linearframework.web.internal.ServerRegistrant
import spark.Spark

/**
 * Allows for dealing with 404 responses.
 *
 * There must be only one NotFoundHandler in the scope of the server.
 *
 * In order to be automatically registered with the server,
 * your handler must be a top-level Scala object and cannot be a nested/internal objects class, etc.
 */
trait NotFoundHandler extends ServerRegistrant {

  /**
   * Handles a 404 request
   */
  def apply(request: Request, response: Response): Unit

  abstract override private[web] def register(): Unit = {
    Spark.notFound(route)
  }

  private lazy val route: spark.Route = {
    (request: spark.Request, response: spark.Response) => {
      val req = new RequestImpl(request)
      val res = new ResponseImpl(response)
      res.setStatus(NOT_FOUND)
      apply(req, res)
      res.getBody.getOrElse("")
    }
  }

}

