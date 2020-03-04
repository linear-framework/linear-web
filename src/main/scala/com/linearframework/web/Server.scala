package com.linearframework.web

import com.linearframework.web.internal._
import org.slf4j.{Logger, LoggerFactory}
import spark.Spark
import spark.utils.SparkUtils
import java.util

/**
 * Convenience methods for configuring and starting a [[com.linearframework.web.Server]].
 */
object Server {

  /**
   * Configures a new server to auto-scan for Controllers, Filters, and other
   * Components in the given package
   * @param pkg the package to scan for server components
   */
  def autoScan(pkg: String): Configuration = {
    Configuration(pkg)
  }

  /**
   * Configures a new Server instance
   */
  case class Configuration(
    override val autoScan: String
  ) extends ServerConfiguration(autoScan) {
    private lazy val conf = this

    override def start(): Server = {
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
          override private[web] val deserializers: util.Map[ContentType, RequestBodyDeserializer] = conf.deserializers
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
  protected val log: Logger = LoggerFactory.getLogger(this.getClass)

  protected val autoScanPackage: String
  protected val scheme: String
  protected val host: String
  protected val port: Int
  protected val corsAllowedOrigin: Option[String]
  protected val logRequests: Boolean
  protected val staticFiles: Option[StaticFilesConfiguration]
  protected val ssl: Option[SslConfiguration]
  private[web] val registry: util.Map[Class[_], util.Set[_]]
  private[web] val deserializers: util.Map[ContentType, RequestBodyDeserializer]

  /**
   * Starts this server
   */
  final def start(): Unit = {
    log.info("Starting web server...")

    applyServerSettings()
    registerFilters()
    registerExceptionHandlers()
    registerControllers()
    afterControllersRegistered()
    register404Handler()
    registerComponents()

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
      if (config.location == EXTERNAL) {
        Spark.staticFiles.externalLocation(config.path)
      }
      else {
        Spark.staticFiles.location(config.path)
      }
    }
  }

  private def registerControllers(): Unit = {
    var controllers = Utils.findScalaObjects[Controller](autoScanPackage)

    if (corsAllowedOrigin.isDefined) {
      controllers = controllers :+ CorsController
    }

    val endpoints =
      controllers
        .flatMap(controller => {
          controller.setServer(this)
          controller.register()
          controller.endpointRegistry.map { endpoint =>
            Seq(
              endpoint.webMethod.method.toString,
              endpoint.webMethod.path,
              controller.getClass.getName.replaceAllLiterally("$", "")
            )
          }
        })

    log.info("Registered Endpoints:")
    logTable(Seq("METHOD", "PATH", "CLASS"), endpoints)
  }

  private def registerFilters(): Unit = {
    var filters = Utils.findScalaObjects[Filter](autoScanPackage)

    if (corsAllowedOrigin.isDefined) {
      // CORS filter must be registered before all other filters
      filters = Seq(new CorsFilter(corsAllowedOrigin.get)) ++ filters
    }

    if (logRequests) {
      filters = filters :+ RequestLogger
    }

    val registered =
      filters.flatMap(filter => {
        filter.setServer(this)
        filter.register()
        filter.filterRegistry.map { method =>
          Seq(
            method.method.toString,
            if (method.path == SparkUtils.ALL_PATHS) "*" else method.path,
            filter.getClass.getName.replaceAllLiterally("$", "")
          )
        }
      })

    log.info("Registered Filters:")
    logTable(Seq("METHOD", "PATH", "CLASS"), registered)
  }

  private def registerExceptionHandlers(): Unit = {
    var handlers = Utils.findScalaObjects[ExceptionHandler[_]](autoScanPackage)

    if (!handlers.exists(_.exceptionClass == classOf[Exception])) {
      handlers = handlers :+ DefaultExceptionHandler
    }

    val registered =
      handlers.map(handler => {
        handler.setServer(this)
        handler.register()
        Seq(
          handler.exceptionClass.getName.replaceAllLiterally("$", ""),
          handler.getClass.getName.replaceAllLiterally("$", "")
        )
      })

    log.info("Registered Exception Handlers:")
    logTable(Seq("EXCEPTION TYPE", "HANDLER"), registered)
  }

  private def register404Handler(): Unit = {
    val handlers = Utils.findScalaObjects[NotFoundHandler](autoScanPackage)
    var handler: NotFoundHandler = DefaultNotFoundHandler
    if (handlers.length > 1) {
      throw new IllegalStateException("Multiple NotFoundHandlers are present in the scope of this server")
    }
    else if (handlers.length == 1) {
      handler = handlers.head
    }

    handler.setServer(this)
    handler.register()

    log.info(s"Registered 404 Handler:")
    logTable(Seq("HANDLER"), Seq(Seq(s"${handler.getClass.getName.replaceAllLiterally("$", "")}")))
  }

  private def registerComponents(): Unit = {
    val components = Utils.findScalaObjects[Component](autoScanPackage)

    val registered =
      components.map(component => {
        component.setServer(this)
        component.register()
        Seq(
          component.getClass.getName.replaceAllLiterally("$", "")
        )
      })

    log.info("Registered Components:")
    logTable(Seq("COMPONENT"), registered)
  }

  /**
   * Lifecycle hook which enables the execution or arbitrary code after
   * the server has registered all controllers
   */
  protected def afterControllersRegistered(): Unit = {

  }

  /**
   * Lifecycle hook which enables the execution of arbitrary code after the
   * server starts up successfully.
   */
  protected def onStartup(): Unit = {

  }

  protected def logTable(columns: Seq[String], rows: Seq[Seq[String]]): Unit = {
    if (rows.isEmpty) {
      log.info("    [NONE]")
    }
    else {
      Utils.printableTable(columns, rows).linesIterator.foreach(line => log.info("    " + line))
    }
  }

}
