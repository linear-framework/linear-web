package com.linearframework.web.internal

import com.linearframework.web._
import org.slf4j.{Logger, LoggerFactory}

private[web] object DefaultExceptionHandler extends ExceptionHandler[Exception] {
  private val log: Logger = LoggerFactory.getLogger(this.getClass)
  override val exceptionClass: Class[Exception] = classOf[Exception]

  override def apply(e: Exception, request: Request, response: Response): Unit = {
    log.error("An unhandled exception was returned to the client", e)

    e match {
      case http: HttpStatusException =>
        response.setStatus(http.status)
        response.setContentType(HTML)
        response.setBody(s"<h1>${http.status.code} ${http.status.code}</h1>")
      case _ =>
        response.setStatus(INTERNAL_SERVER_ERROR)
        response.setContentType(HTML)
        response.setBody(s"<h1>${INTERNAL_SERVER_ERROR.code} ${INTERNAL_SERVER_ERROR.reason}</h1>")
    }
  }
}
