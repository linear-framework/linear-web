package com.linearframework.web

import javax.servlet.http.HttpSession
import java.time.{Instant, LocalDateTime, ZoneId}
import scala.jdk.CollectionConverters._

/**
 * Represents a session created by the server
 */
trait Session {

  /**
   * Gets the raw HttpSession object created by the servlet container
   */
  def raw(): HttpSession

  /**
   * Gets the session id
   */
  def getId: String

  /**
   * Gets the names of all attributes available on the session
   */
  def getAttributeNames: Set[String]

  /**
   * Gets the attribute from the session with the given name
   * @param name the name of the attribute to fetch
   */
  def getAttribute[T](name: String): Option[T]

  /**
   * Sets an attribute on the session
   * @param name the name of the attribute
   * @param value the value of the attribute
   */
  def setAttribute(name: String, value: AnyRef): Unit

  /**
   * Removes an attribute from the session
   * @param name the name of the attribute to remove
   */
  def removeAttribute(name: String): Unit

  /**
   * Gets the last time the client sent a request associated with this session
   */
  def getLastAccessedTime: LocalDateTime

  /**
   * Gets the number of seconds that the session will be kept open between client accesses
   */
  def getMaxInactiveInterval: Int

  /**
   * Sets the number of seconds that the session will be kept open between client accesses
   * @param seconds the interval to keep a session open
   */
  def setMaxInactiveInterval(seconds: Int): Unit

  /**
   * Invalidates this session
   */
  def invalidate(): Unit

  /**
   * Whether or not the client has joined the session
   */
  def isNew: Boolean

}

private[web] class SessionImpl private[web](private val inner: spark.Session) extends Session {
  override def raw(): HttpSession = inner.raw()

  override def getId: String = inner.id()

  override def getAttributeNames: Set[String] = inner.attributes().asScala.toSet

  override def getAttribute[T](name: String): Option[T] = Option(inner.attribute(name))

  override def setAttribute(name: String, value: AnyRef): Unit = inner.attribute(name, value)

  override def removeAttribute(name: String): Unit = inner.removeAttribute(name)

  override def getLastAccessedTime: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(inner.lastAccessedTime()), ZoneId.systemDefault())

  override def getMaxInactiveInterval: Int = inner.maxInactiveInterval()

  override def setMaxInactiveInterval(seconds: Int): Unit = inner.maxInactiveInterval(seconds)

  override def invalidate(): Unit = inner.invalidate()

  override def isNew: Boolean = inner.isNew
}