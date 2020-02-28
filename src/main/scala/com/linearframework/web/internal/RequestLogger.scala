package com.linearframework.web.internal

import com.linearframework.web._
import org.slf4j.{Logger, LoggerFactory}

private[web] object RequestLogger extends Filter {
  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  BEFORE { (request, _) =>
    log.debug(s"REQUEST [${request.getRequestMethod} ${request.getPathInfo.getOrElse("")}] ${request.getBody}")
  }

  AFTER_AFTER { (request, response) =>
    log.debug(s"RESPONSE ${response.getStatus.code} [${request.getRequestMethod} ${request.getPathInfo.getOrElse("")}] ${response.getBody.getOrElse("")}")
  }

}

