package com.linearframework.web

/**
 * Represents a standard HTTP Verb
 */
sealed trait HttpVerb

object HttpVerb {
  case object GET extends HttpVerb
  case object POST extends HttpVerb
  case object PUT extends HttpVerb
  case object DELETE extends HttpVerb
  case object HEAD extends HttpVerb
  case object TRACE extends HttpVerb
  case object CONNECT extends HttpVerb
  case object OPTIONS extends HttpVerb

  def apply(verb: String): HttpVerb = {
    verb.toUpperCase.trim match {
      case "GET" => GET
      case "POST" => POST
      case "PUT" => PUT
      case "DELETE" => DELETE
      case "HEAD" => HEAD
      case "TRACE" => TRACE
      case "CONNECT" => CONNECT
      case "OPTIONS" => OPTIONS
      case x => throw new IllegalStateException(s"HTTP verb [$x] is unsupported")
    }
  }
}