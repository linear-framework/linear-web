package com.linearframework.web

import com.linearframework.web.internal.Cookie
import javax.servlet.http.HttpServletResponse
import java.time.ZonedDateTime

/**
 * Represents a REST HTTP response
 */
trait Response {

  /**
   * Fetches the body of the response
   * @return the body of the response, if available, or [[scala.None]] if it was not found
   */
  def getBody: Option[String]

  /**
   * Sets the body of the response
   */
  def setBody(body: String): Unit

  /**
   * Sets an HTTP header on the response
   * @param name the name of the header to set
   * @param value the value of the header to set
   */
  def setHeader(name: String, value: String): Unit

  /**
   * Gets the current status of the response
   */
  def getStatus: HttpStatus

  /**
   * Sets the status of the response
   */
  def setStatus(status: HttpStatus): Unit

  /**
   * Gets the current content type of the response, if one has been set
   * @return the content type of the response, if available, or [[scala.None]] if it was not found
   */
  def getContentType: Option[ContentType]

  /**
   * Sets the content type of the response
   */
  def setContentType(contentType: ContentType): Unit

  /**
   * Gets the underlying [[javax.servlet.http.HttpServletResponse]]
   */
  def raw: HttpServletResponse

  /**
   * Sets a cookie on the response
   * @param name     name of the cookie
   * @param value    value of the cookie
   * @param maxAge   max age of the cookie in seconds
   * @param expires  the expiration date of the cookie (if both maxAge and expires are provided, only maxAge will be used)
   * @param secured  if true: cookie will be secured
   * @param httpOnly if true: cookie will be marked as http only
   * @param sameSite provides browser-based XSRF protection if enabled
   * @param domain   domain of the cookie
   * @param path     path of the cookie
   */
  def setCookie(
    name: String,
    value: String,
    maxAge: Int = -1,
    expires: ZonedDateTime = null,
    secured: Boolean = false,
    httpOnly: Boolean = false,
    sameSite: SameSite = null,
    domain: String = null,
    path: String = null
  ): Unit

  /**
   * Removes the cookie with the given name
   * @param name name of the cookie
   */
  def removeCookie(name: String): Unit

}


private[web] class ResponseImpl private[web](private val inner: spark.Response) extends Response {

  override def getBody: Option[String] = Option(inner.body())

  override def setBody(body: String): Unit = inner.body(body)

  override def setHeader(name: String, value: String): Unit = inner.header(name, value)

  override def getStatus: HttpStatus = HttpStatus(inner.status())

  override def setStatus(status: HttpStatus): Unit = inner.status(status.code)

  override def getContentType: Option[ContentType] = Option(ContentType(inner.`type`()))

  override def setContentType(contentType: ContentType): Unit = inner.`type`(contentType.toString)

  override def raw: HttpServletResponse = inner.raw()

  override def setCookie(name: String, value: String, maxAge: Int, expires: ZonedDateTime, secured: Boolean, httpOnly: Boolean, sameSite: SameSite, domain: String, path: String): Unit = {
    inner.header(
      "Set-Cookie",
      new Cookie(
        name,
        value,
        Option(maxAge),
        Option(expires),
        Option(secured),
        Option(httpOnly),
        Option(sameSite),
        Option(domain),
        Option(path)
      ).toString
    )
  }

  override def removeCookie(name: String): Unit = {
    inner.header(
      "Set-Cookie",
      new Cookie(
        name,
        null,
        None,
        Some(ZonedDateTime.now().minusYears(1)),
        None,
        None,
        None,
        None,
        None
      ).toString
    )
  }

}