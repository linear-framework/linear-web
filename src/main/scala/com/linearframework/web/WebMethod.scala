package com.linearframework.web

/**
 * Represents an endpoint in the web server
 */
case class WebMethod private[web](method: HttpVerb, path: String)
