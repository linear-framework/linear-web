package com.linearframework.web

import com.linearframework.web.internal.ServerRegistrant
import spark.Spark
import scala.collection.mutable

/**
 * Defines REST endpoints.
 * <br/><br/>
 * A sample controller looks like this:
 * {{{
 * object GreetingsController extends Controller {
 *   GET("/hello") { (request, response) =>
 *     "hello"
 *   }
 *   GET("/goodbye") { (request, response) =>
 *     "goodbye"
 *   }
 * }
 * }}}
 * In order to be automatically registered with the server,
 * controllers must be top-level Scala objects and cannot be nested/internal objects, classes, etc.
 */
trait Controller extends ServerRegistrant {
  private[web] val endpointRegistry = mutable.Set[Endpoint[_]]()

  private[web] class Endpoint[T <: AnyRef](
    val method: HttpVerb,
    val path: String,
    val handler: (Request, Response) => T,
    val transformer: ResponseTransformer
  ) {
    override def hashCode(): Int = {
      s"$method $path".hashCode
    }
    override def equals(obj: Any): Boolean = {
      obj match {
        case that: Endpoint[T] =>
          this.method == that.method &&
            this.path == that.path
        case _ =>
          false
      }
    }
  }

  protected implicit class MethodImplicits(verb: HttpVerb) {
    def apply[T <: AnyRef](path: String)(handler: (Request, Response) => T)(implicit transformer: ResponseTransformer = (result, _) => result): WebMethod = {
      val endpoint = new Endpoint(verb, path, handler, transformer)
      endpointRegistry += endpoint
      new WebMethod()
    }
  }

  abstract override private[web] def register(): Unit = {
    super.register()
    endpointRegistry.foreach { endpoint =>
      val route: spark.Route = (request: spark.Request, response: spark.Response) => {
        val req = new RequestImpl(request)
        val res = new ResponseImpl(response)
        val result = endpoint.handler(req, res).asInstanceOf[AnyRef]
        endpoint.transformer(result, res)
      }

      endpoint.method match {
        case GET => Spark.get(endpoint.path, route)
        case POST => Spark.post(endpoint.path, route)
        case PUT => Spark.put(endpoint.path, route)
        case DELETE => Spark.delete(endpoint.path, route)
        case HEAD => Spark.head(endpoint.path, route)
        case TRACE => Spark.trace(endpoint.path, route)
        case CONNECT => Spark.connect(endpoint.path, route)
        case OPTIONS => Spark.options(endpoint.path, route)
      }
    }
  }
}


