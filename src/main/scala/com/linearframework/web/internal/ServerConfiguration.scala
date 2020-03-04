package com.linearframework.web.internal

import com.linearframework.web._
import java.util
import scala.reflect.ClassTag

/**
 * Configures a new Server instance
 */
private[web] abstract class ServerConfiguration(
  val autoScan: String,
  protected var scheme: String = "http",
  protected var host: String = "localhost",
  protected var port: Int = 4567,
  protected var corsOrigin: Option[String] = None,
  protected var logRequests: Boolean = false,
  protected var staticFiles: Option[StaticFilesConfiguration] = None,
  protected var ssl: Option[SslConfiguration] = None,
  protected var registry: util.Map[Class[_], util.Set[_]] = new util.HashMap[Class[_], util.Set[_]](),
  protected var startupHook: () => Unit = () => { },
  protected var deserializers: util.Map[ContentType, RequestBodyDeserializer] = {
    val map = new util.HashMap[ContentType, RequestBodyDeserializer]()
    map.put(JSON, JacksonJsonRequestBodyDeserializer)
    map.put(XML, JacksonXmlRequestBodyDeserializer)
    map.put(APPLICATION_XML, JacksonXmlRequestBodyDeserializer)
    map.put(FORM_DATA, FormUrlEncodedBodyDeserializer)
    map
  }
) {

  /**
   * Sets the host name
   */
  def host(host: String): this.type = {
    this.host = host
    this
  }

  /**
   * Sets the port
   */
  def port(port: Int): this.type = {
    this.port = port
    this
  }

  /**
   * Enables CORS and sets the allowed origin
   */
  def corsOrigin(origin: String): this.type = {
    this.corsOrigin = Option(origin)
    this
  }

  /**
   * Enables or disables request logging
   */
  def logRequests(enabled: Boolean): this.type = {
    this.logRequests = enabled
    this
  }

  /**
   * Enables serving of static files, either from the classpath or an external location
   */
  def staticFiles(from: StaticFileLocation, path: String): this.type = {
    this.staticFiles = Some(StaticFilesConfiguration(from, path))
    this
  }

  /**
   * Enables SSL over HTTPS
   */
  def secure(keystoreFilePath: String, keystorePassword: String, truststoreFilePath: String = null, truststorePassword: String = null): this.type = {
    this.scheme = "https"
    this.ssl = Some(SslConfiguration(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword))
    this
  }

  /**
   * Adds (or overrides) a deserializer for the given content type.
   */
  def setDeserializer(contentType: ContentType, deserializer: RequestBodyDeserializer): this.type = {
    deserializers.put(contentType, deserializer)
    this
  }

  /**
   * Registers an object with the server under a parent class.
   * Registered objects are available to all server components using `the[Class]` and `all[Class]` methods.
   */
  def registerUnder[CLASS: ClassTag, OBJECT <: CLASS](obj: OBJECT)(implicit classTag: ClassTag[CLASS]): this.type = {
    val clazz = classTag.runtimeClass
    if (!registry.containsKey(clazz)) {
      registry.put(clazz, new util.LinkedHashSet[CLASS]())
    }
    registry.get(clazz).asInstanceOf[util.Set[CLASS]].add(obj)
    this
  }

  /**
   * Registers an object with the server.
   * Registered objects are available to all server components using `the[Class]` and `all[Class]` methods.
   */
  def register[CLASS: ClassTag](obj: CLASS)(implicit classTag: ClassTag[CLASS]): this.type = {
    val clazz = classTag.runtimeClass
    if (!registry.containsKey(clazz)) {
      registry.put(clazz, new util.LinkedHashSet[CLASS]())
    }
    registry.get(clazz).asInstanceOf[util.Set[CLASS]].add(obj)
    this
  }

  /**
   * Executes the hook after the server successfully starts up
   */
  def startupHook(hook: () => Unit): this.type = {
    this.startupHook = hook
    this
  }

  /**
   * Builds and starts the server
   */
  def start(): Server
}
