package com.linearframework.web

import javax.servlet.http.HttpServletRequest
import java.util.Locale
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

/**
 * Represents a REST HTTP request
 */
trait Request {

  /**
   * Converts the body of the request to an instance of type T.
   * The conversion is dependent on the content type set on the request.
   * The content types XML, JSON, and FORM_DATA are supported.  Other content
   * types may raise an exception.
   * @tparam T a case class
   */
  def as[T <: Product with Serializable](implicit classtag: ClassTag[T]): T

  /**
   * Gets the names of all attributes on the request
   */
  def getAttributeNames: Set[String]

  /**
   * Fetches the given attribute from the request
   * @param name the name of the attribute to fetch
   * @tparam T the type of the attribute to fetch
   * @return the attribute value, if available, or [[scala.None]] if it was not found
   */
  def getAttribute[T](name: String): Option[T]

  /**
   * Sets an attribute on the request
   * @param name the name of the attribute (by convention, it should be formatted like a package name, such as `com.example.AuthorizedUser`)
   * @param value the object to store as a request attribute
   */
  def setAttribute[T](name: String, value: T): Unit

  /**
   * Removes the given attribute from the request
   * @param name the name of the attribute to remove
   */
  def removeAttribute(name: String): Unit

  /**
   * Gets the request body
   */
  def getBody: String

  /**
   * Gets the request body as a byte array
   */
  def getBodyAsBytes: Array[Byte]

  /**
   * Gets the IP address of the client making the request
   */
  def getClientIP: String

  /**
   * Gets the content length of the request, if it is known
   */
  def getContentLength: Option[Int]

  /**
   * Gets the content type of the request, if it is known
   */
  def getContentType: Option[ContentType]

  /**
   * Gets the context path of the request
   * @see [[javax.servlet.http.HttpServletRequest#getContextPath()]]
   */
  def getContextPath: String

  /**
   * Gets the cookie with the given name from the request
   */
  def getCookie(name: String): Option[String]

  /**
   * Gets cookies from the request
   */
  def getCookies: Map[String, String]

  /**
   * Gets the names of all of the headers on the request
   */
  def getHeaderNames: Set[String]

  /**
   * Fetches the given header from the request
   * @param name the name of the header to fetch
   * @return the header value, if available, or [[scala.None]] if it was not found
   */
  def getHeader(name: String): Option[String]

  /**
   * Gets the "Host" header from the request, if available
   */
  def getHost: Option[String]

  /**
   * Gets the path info of the request, if present
   * @see [[javax.servlet.http.HttpServletRequest#getPathInfo()]]
   */
  def getPathInfo: Option[String]

  /**
   * Gets all path parameters of the request
   */
  def getPathParams: Map[String, String]

  /**
   * Fetches the given path parameter from the request
   * @param name the name of the parameter to fetch
   * @return the parameter value, if available, or [[scala.None]] if not
   */
  def getPathParam(name: String): Option[String]

  /**
   * Gets the name and version of the request protocol
   */
  def getProtocol: String

  /**
   * Gets a map of all query parameters from this request
   */
  def getQueryParamMap: Map[String, List[String]]

  /**
   * Gets the names of all query parameeters on this request
   */
  def getQueryParamKeys: Set[String]

  /**
   * Fetches the first available value of the given query parameter from the request
   * @param name the name of the query parameter to fetch
   * @return the parameter value, if available, or [[scala.None]] if not.
   *         If multiple values for the named parameter are available, only the first is returned.
   */
  def getQueryParam(name: String): Option[String]

  /**
   * Fetches all available values of the given query parameter from the request
   * @param name the name of the query parameter to fetch
   * @return the parameter values
   */
  def getQueryParams(name: String): List[String]

  /**
   * Gets the method of the request
   */
  def getRequestMethod: HttpVerb

  /**
   * Gets the scheme of the request
   */
  def getScheme: String

  /**
   * Gets the server port
   */
  def getServerPort: Int

  /**
   * Gets the servlet path of this request
   * @see [[javax.servlet.http.HttpServletRequest#getServletPath()]]
   */
  def getServletPath: String

  /**
   * Gets the URI of the request, if available
   */
  def getUri: Option[String]

  /**
   * Gets the URL of the request
   */
  def getUrl: String

  /**
   * Gets the user agent string from the request, if available
   */
  def getUserAgent: Option[String]

  /**
   * Gets the session associated with this request, creating one if it does not already exist
   */
  def getSession: Session

  /**
   * Gets the locale of the request
   */
  def getLocale: Locale

  /**
   * Returns the underlying [[javax.servlet.http.HttpServletRequest]]
   */
  def raw(): HttpServletRequest

}


private[web] class RequestImpl private[web](private val inner: spark.Request, private val deserializers: Map[ContentType, RequestBodyDeserializer]) extends Request {

  override def as[T <: Product with Serializable](implicit classTag: ClassTag[T]): T = {
    getContentType match {
      case Some(contentType) =>
        deserializers.get(contentType) match {
          case Some(deserialize) =>
            deserialize(getBody)
          case None =>
            throw new IllegalStateException(s"No deserializer is available for content type [$contentType]")
        }
      case None =>
        throw new IllegalStateException("Request is missing Content-Type header; cannot deserialize to object.")
    }
  }

  override def getAttributeNames: Set[String] = inner.attributes().asScala.toSet

  override def getAttribute[T](name: String): Option[T] = Option(inner.attribute[T](name))

  override def setAttribute[T](name: String, value: T): Unit = inner.attribute(name, value)

  override def removeAttribute(name: String): Unit = inner.attribute(name, null)

  override def getBody: String = inner.body()

  override def getBodyAsBytes: Array[Byte] = inner.bodyAsBytes()

  override def getClientIP: String = inner.ip()

  override def getContentLength: Option[Int] = if (inner.contentLength() >= 0) Some(inner.contentLength()) else None

  override def getContentType: Option[ContentType] = if (inner.contentType() == null) None else Some(ContentType(inner.contentType()))

  override def getContextPath: String = inner.contextPath()

  override def getCookie(name: String): Option[String] = Option(inner.cookie(name))

  override def getCookies: Map[String, String] = inner.cookies().asScala.toMap

  override def getHeaderNames: Set[String] = inner.headers().asScala.toSet

  override def getHeader(name: String): Option[String] = Option(inner.headers(name))

  override def getHost: Option[String] = Option(inner.host())

  override def getPathInfo: Option[String] = Option(inner.pathInfo())

  override def getPathParams: Map[String, String] = inner.params().asScala.toMap

  override def getPathParam(name: String): Option[String] = Option(inner.params(name))

  override def getProtocol: String = inner.protocol()

  override def getQueryParamMap: Map[String, List[String]] = inner.raw().getParameterMap.asScala.toMap.map { case (key, values) => key -> values.toList }

  override def getQueryParamKeys: Set[String] = inner.queryParams().asScala.toSet

  override def getQueryParam(name: String): Option[String] = Option(inner.queryParams(name))

  override def getQueryParams(name: String): List[String] = Option(inner.queryParamsValues(name).toList).getOrElse(List())

  override def getRequestMethod: HttpVerb = HttpVerb(inner.requestMethod())

  override def getScheme: String = inner.scheme()

  override def getServerPort: Int = inner.port()

  override def getServletPath: String = inner.servletPath()

  override def getUri: Option[String] = Option(inner.uri())

  override def getUrl: String = inner.url()

  override def getUserAgent: Option[String] = Option(inner.userAgent())

  override def getSession: Session = new SessionImpl(inner.session(true))

  override def getLocale: Locale = raw().getLocale

  override def raw(): HttpServletRequest = inner.raw()

}