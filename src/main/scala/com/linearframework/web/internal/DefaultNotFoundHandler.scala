package com.linearframework.web.internal

import com.linearframework.web.ContentType.HTML
import com.linearframework.web.HttpStatus.NOT_FOUND
import com.linearframework.web._

private[web] object DefaultNotFoundHandler extends NotFoundHandler {
  override def apply(request: Request, response: Response): Unit = {
    response.setStatus(NOT_FOUND)
    response.setContentType(HTML)
    response.setBody(s"<h1>${NOT_FOUND.code} ${NOT_FOUND.reason}</h1>")
  }
}
