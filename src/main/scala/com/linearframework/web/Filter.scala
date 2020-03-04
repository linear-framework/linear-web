package com.linearframework.web

import com.linearframework.web.internal.ServerRegistrant
import spark.Spark
import spark.utils.SparkUtils
import scala.collection.mutable

/**
 * Defines request filters.
 * <br/><br/>
 * A sample filter looks like this:
 * {{{
 * object AuthFilter extends Filter {
 *   BEFORE("/admin/checkAuthorization") { (request, response) =>
 *     if (request.getHeader("Authorization").isEmpty) {
 *       throw new UnauthorizedException()
 *     }
 *   }
 *   AFTER { (request, response) =>
 *     response.setHeader("X-Application-Name", "My App")
 *   }
 * }
 * }}}
 * The path is optional; if not specified, the filter will be applied to all paths.
 * <br/><br/>
 * In order to be automatically registered with the server,
 * filters must be top-level Scala objects and cannot be nested/internal objects, classes, etc.
 */
trait Filter extends ServerRegistrant {
  private[web] val filterRegistry = mutable.ListBuffer[Filter]()

  private[web] case class Filter(
    method: FilterMethod,
    path: String,
    handler: (Request, Response) => Unit
  )

  protected implicit class FilterMethodImplicits(method: FilterMethod) {
    def apply[T <: AnyRef](path: String)(handler: (Request, Response) => Unit): Unit = {
      filterRegistry += Filter(method, path, handler)
    }
    def apply[T <: AnyRef](handler: (Request, Response) => Unit): Unit = {
      filterRegistry += Filter(method, SparkUtils.ALL_PATHS, handler)
    }
  }

  abstract override private[web] def register(): Unit = {
    super.register()
    filterRegistry.foreach { filter =>
      val sparkFilter: spark.Filter = (request: spark.Request, response: spark.Response) => {
        val req = new RequestImpl(request, deserializers)
        val res = new ResponseImpl(response)
        filter.handler(req, res)
      }

      filter.method match {
        case BEFORE => Spark.before(filter.path, sparkFilter)
        case AFTER => Spark.after(filter.path, sparkFilter)
        case AFTER_AFTER => Spark.afterAfter(filter.path, sparkFilter)
      }
    }
  }
}

