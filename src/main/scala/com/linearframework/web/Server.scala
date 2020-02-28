package com.linearframework.web

import com.linearframework.web.internal._
import org.slf4j.{Logger, LoggerFactory}
import spark.Spark
import spark.utils.SparkUtils
import java.util
import scala.reflect.ClassTag

/**
 * Convenience methods for configuring and starting a [[com.linearframework.web.Server]].
 */
object Server {

  /**
   * Configures a new server to auto-scan for Controllers, Filters, and other
   * Components in the given package
   * @param pkg the package to scan for server components
   */
  def autoScan(pkg: String): ServerConfiguration = {
    ServerConfiguration(pkg)
  }

  /**
   * Configures a new Server instance
   */
  case class ServerConfiguration(
    private val autoScan: String,
    private val scheme: String = "http",
    private val host: String = "localhost",
    private val port: Int = 4567,
    private val corsOrigin: Option[String] = None,
    private val logRequests: Boolean = false,
    private val staticFiles: Option[StaticFilesConfiguration] = None,
    private val ssl: Option[SslConfiguration] = None,
    private val registry: util.Map[Class[_], util.Set[_]] = new util.HashMap[Class[_], util.Set[_]](),
    private val startupHook: () => Unit = () => { }
  ) {
    private val conf = this

    /**
     * Sets the host name
     */
    def host(host: String): ServerConfiguration = {
      this.copy(host = host)
    }

    /**
     * Sets the port
     */
    def port(port: Int): ServerConfiguration = {
      this.copy(port = port)
    }

    /**
     * Enables CORS and sets the allowed origin
     */
    def corsOrigin(origin: String): ServerConfiguration = {
      this.copy(corsOrigin = Option(origin))
    }

    /**
     * Enables or disables request logging
     */
    def logRequests(enabled: Boolean): ServerConfiguration = {
      this.copy(logRequests = enabled)
    }

    /**
     * Enables serving of static files, either from the classpath or an external location
     */
    def staticFiles(from: StaticFileLocation, path: String): ServerConfiguration = {
      this.copy(staticFiles = Some(StaticFilesConfiguration(from, path)))
    }

    /**
     * Enables SSL over HTTPS
     */
    def secure(keystoreFilePath: String, keystorePassword: String, truststoreFilePath: String, truststorePassword: String): ServerConfiguration = {
      this.copy(
        scheme = "https",
        ssl = Some(SslConfiguration(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword))
      )
    }

    /**
     * Registers an object with the server under a parent class.
     * Registered objects are available to all server components using `the[Class]` and `all[Class]` methods.
     */
    def registerUnder[CLASS: ClassTag, OBJECT <: CLASS](obj: OBJECT)(implicit classTag: ClassTag[CLASS]): ServerConfiguration = {
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
    def register[CLASS: ClassTag](obj: CLASS)(implicit classTag: ClassTag[CLASS]): ServerConfiguration = {
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
    def startupHook(hook: () => Unit): ServerConfiguration = {
      this.copy(startupHook = hook)
    }

    /**
     * Builds and starts the server
     */
    def start(): Server = {
      val server =
        new Server {
          override protected val autoScanPackage: String = conf.autoScan
          override protected val scheme: String = conf.scheme
          override protected val host: String = conf.host
          override protected val port: Int = conf.port
          override protected val corsAllowedOrigin: Option[String] = conf.corsOrigin
          override protected val logRequests: Boolean = conf.logRequests
          override protected val staticFiles: Option[StaticFilesConfiguration] = conf.staticFiles
          override protected val ssl: Option[SslConfiguration] = conf.ssl
          override private[web] val registry = conf.registry
        }
      server.start()
      server
    }
  }
}

/**
 * A simple web server
 */
trait Server {
  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  protected val autoScanPackage: String
  protected val scheme: String
  protected val host: String
  protected val port: Int
  protected val corsAllowedOrigin: Option[String]
  protected val logRequests: Boolean
  protected val staticFiles: Option[StaticFilesConfiguration]
  protected val ssl: Option[SslConfiguration]
  private[web] val registry: util.Map[Class[_], util.Set[_]]

  /**
   * Starts this server
   */
  final def start(): Unit = {
    log.info("Starting web server...")

    applyServerSettings()
//    registerFilters()
//    registerExceptionHandlers()
    registerControllers()
//    register404Handler()
//    registerComponents()

    Spark.init()
    Spark.awaitInitialization()

    onStartup()

    log.info("Web server initialization complete")
    log.info(s"Running on $scheme://$host:$port/")
  }

  /**
   * Stops this server
   */
  final def stop(): Unit = {
    Spark.stop()
    Spark.awaitStop()
  }

  private def applyServerSettings(): Unit = {
    Spark.port(port)

    if (ssl.isDefined) {
      log.info("Enabling SSL over HTTPS")
      val config = ssl.get
      Spark.secure(config.keystoreFilePath, config.keystorePassword, config.truststoreFilePath, config.truststorePassword)
    }

    if (staticFiles.isDefined) {
      log.info("Serving Static Files:")
      val config = staticFiles.get
      logTable(Seq("LOCATION", "PATH"), Seq(Seq(config.location.toString, config.path)))
      if (config.location == StaticFileLocation.EXTERNAL) {
        Spark.staticFiles.externalLocation(config.path)
      }
      else {
        Spark.staticFiles.location(config.path)
      }
    }
  }

  private def registerControllers(): Unit = {
    var controllers = Utils.findScalaObjects[Controller](autoScanPackage)

//    if (corsAllowedOrigin.isDefined) {
//      controllers = controllers :+ CorsController
//    }

    val endpoints =
      controllers
        .flatMap(controller => {
          controller.setServer(this)
          controller.register()
          controller.endpointRegistry.map { endpoint =>
            Seq(
              endpoint.method.toString,
              endpoint.path,
              controller.getClass.getName.replaceAllLiterally("$", "")
            )
          }
        })

    log.info("Registered Endpoints:")
    logTable(Seq("METHOD", "PATH", "CLASS"), endpoints)
  }

//  private def registerFilters(): Unit = {
//    var filters = Utils.findScalaObjects[Filter](autoScanPackage)
//
//    if (corsAllowedOrigin.isDefined) {
//      // CORS filter must be registered before all other filters
//      filters = Seq(new CorsFilter(corsAllowedOrigin.get)) ++ filters
//    }
//
//    if (logRequests) {
//      filters = filters :+ RequestLogger
//    }
//
//    val registered =
//      filters.flatMap(filter => {
//        filter.setServer(this)
//        filter.register()
//        filter.filterRegistry.map { method =>
//          Seq(
//            method.method.toString,
//            if (method.path == SparkUtils.ALL_PATHS) "*" else method.path,
//            filter.getClass.getName.replaceAllLiterally("$", "")
//          )
//        }
//      })
//
//    log.info("Registered Filters:")
//    logTable(Seq("METHOD", "PATH", "CLASS"), registered)
//  }

//  private def registerExceptionHandlers(): Unit = {
//    var handlers = Utils.findScalaObjects[ExceptionHandler[_]](autoScanPackage)
//
//    if (!handlers.exists(_.exceptionClass == classOf[Exception])) {
//      handlers = handlers :+ DefaultExceptionHandler
//    }
//
//    val registered =
//      handlers.map(handler => {
//        handler.setServer(this)
//        handler.register()
//        Seq(
//          handler.exceptionClass.getName.replaceAllLiterally("$", ""),
//          handler.getClass.getName.replaceAllLiterally("$", "")
//        )
//      })
//
//    log.info("Registered Exception Handlers:")
//    logTable(Seq("EXCEPTION TYPE", "HANDLER"), registered)
//  }

//  private def register404Handler(): Unit = {
//    val handlers = Utils.findScalaObjects[NotFoundHandler](autoScanPackage)
//    var handler: NotFoundHandler = DefaultNotFoundHandler
//    if (handlers.length > 1) {
//      throw new IllegalStateException("Multiple NotFoundHandlers are present in the scope of this server")
//    }
//    else if (handlers.length == 1) {
//      handler = handlers.head
//    }
//
//    handler.setServer(this)
//    handler.register()
//
//    log.info(s"Registered 404 Handler:")
//    logTable(Seq("HANDLER"), Seq(Seq(s"${handler.getClass.getName.replaceAllLiterally("$", "")}")))
//  }

//  private def registerComponents(): Unit = {
//    var components = Utils.findScalaObjects[Component](autoScanPackage)
//
//    val registered =
//      components.map(component => {
//        component.setServer(this)
//        component.register()
//        Seq(
//          component.getClass.getName.replaceAllLiterally("$", "")
//        )
//      })
//
//    log.info("Registered Components:")
//    logTable(Seq("COMPONENT"), registered)
//  }

  /**
   * Lifecycle hook which enables the execution of arbitrary code after the
   * server starts up successfully.
   */
  protected def onStartup() : Unit = {

  }

  private def logTable(columns: Seq[String], rows: Seq[Seq[String]]): Unit = {
    if (rows.isEmpty) {
      log.info("    [NONE]")
    }
    else {
      Utils.printableTable(columns, rows).linesIterator.foreach(line => log.info("    " + line))
    }
  }

}
