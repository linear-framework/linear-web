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
    val webMethod: WebMethod,
    val handler: (Request, Response) => T,
    val transformer: ResponseTransformer
  ) {
    override def hashCode(): Int = {
      webMethod.hashCode
    }
    override def equals(obj: Any): Boolean = {
      obj match {
        case that: Endpoint[T] =>
          this.webMethod == that.webMethod
        case _ =>
          false
      }
    }
  }

  protected implicit class MethodImplicits(verb: HttpVerb) {
    def apply[T <: AnyRef](path: String)(handler: (Request, Response) => T)(implicit transformer: ResponseTransformer = (result, _) => result): WebMethod = {
      val webMethod = WebMethod(verb, path)
      val endpoint = new Endpoint(webMethod, handler, transformer)
      endpointRegistry += endpoint
      webMethod
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

      endpoint.webMethod.method match {
        case GET => Spark.get(endpoint.webMethod.path, route)
        case POST => Spark.post(endpoint.webMethod.path, route)
        case PUT => Spark.put(endpoint.webMethod.path, route)
        case DELETE => Spark.delete(endpoint.webMethod.path, route)
        case HEAD => Spark.head(endpoint.webMethod.path, route)
        case TRACE => Spark.trace(endpoint.webMethod.path, route)
        case CONNECT => Spark.connect(endpoint.webMethod.path, route)
        case OPTIONS => Spark.options(endpoint.webMethod.path, route)
      }
    }
  }
}


