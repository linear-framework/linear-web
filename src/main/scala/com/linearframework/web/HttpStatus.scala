package com.linearframework.web

/**
 * Represents an HTTP Status code
 */
sealed abstract class HttpStatus(val code: Int, val reason: String) {
  lazy val isInformational: Boolean = {
    code >= 100 && code <= 199
  }

  lazy val isSuccess: Boolean = {
    code >= 100 && code <= 399
  }

  lazy val isFailure: Boolean = {
    code >= 400 && code <= 599
  }

  lazy val isRedirection: Boolean = {
    code >= 300 && code <= 399
  }

  lazy val allowsEntity: Boolean = {
    code != 204 && code != 304 && (isSuccess || isRedirection || isFailure)
  }
}

object HttpStatus {
  case object CONTINUE extends HttpStatus(100, "Continue")
  case object SWITCHING_PROTOCOLS extends HttpStatus(101, "Switching Protocols")
  case object PROCESSING extends HttpStatus(102, "Processing")
  case object EARLY_HINTS extends HttpStatus(103, "Early Hints")
  case object OK extends HttpStatus(200, "OK")
  case object CREATED extends HttpStatus(201, "Created")
  case object ACCEPTED extends HttpStatus(202, "Accepted")
  case object NON_AUTHORITATIVE_INFORMATION extends HttpStatus(203, "Non-Authoritative Information")
  case object NO_CONTENT extends HttpStatus(204, "No Content")
  case object RESET_CONTENT extends HttpStatus(205, "Reset Content")
  case object PARTIAL_CONTENT extends HttpStatus(206, "Partial Content")
  case object MULTI_HTTP_STATUS extends HttpStatus(207, "Multi-HttpStatus")
  case object ALREADY_REPORTED extends HttpStatus(208, "Already Reported")
  case object IM_USED extends HttpStatus(226, "IM Used")
  case object MULTIPLE_CHOICES extends HttpStatus(300, "Multiple Choices")
  case object MOVED_PERMANENTLY extends HttpStatus(301, "Moved Permanently")
  case object FOUND extends HttpStatus(302, "Found")
  case object SEE_OTHER extends HttpStatus(303, "See Other")
  case object NOT_MODIFIED extends HttpStatus(304, "Not Modified")
  case object USE_PROXY extends HttpStatus(305, "Use Proxy")
  case object TEMPORARY_REDIRECT extends HttpStatus(307, "Temporary Redirect")
  case object PERMANENT_REDIRECT extends HttpStatus(308, "Permanent Redirect")
  case object BAD_REQUEST extends HttpStatus(400, "Bad Request")
  case object UNAUTHORIZED extends HttpStatus(401, "Unauthorized")
  case object PAYMENT_REQUIRED extends HttpStatus(402, "Payment Required")
  case object FORBIDDEN extends HttpStatus(403, "Forbidden")
  case object NOT_FOUND extends HttpStatus(404, "Not Found")
  case object METHOD_NOT_ALLOWED extends HttpStatus(405, "Method Not Allowed")
  case object NOT_ACCEPTABLE extends HttpStatus(406, "Not Acceptable")
  case object PROXY_AUTHENTICATION_REQUIRED extends HttpStatus(407, "Proxy Authentication Required")
  case object REQUEST_TIMEOUT extends HttpStatus(408, "Request Timeout")
  case object CONFLICT extends HttpStatus(409, "Conflict")
  case object GONE extends HttpStatus(410, "Gone")
  case object LENGTH_REQUIRED extends HttpStatus(411, "Length Required")
  case object PRECONDITION_FAILED extends HttpStatus(412, "Precondition Failed")
  case object REQUEST_ENTITY_TOO_LARGE extends HttpStatus(413, "Request Entity Too Large")
  case object REQUEST_URI_TOO_LONG extends HttpStatus(414, "Request-URI Too Long")
  case object UNSUPPORTED_MEDIA_TYPE extends HttpStatus(415, "Unsupported Media Type")
  case object REQUESTED_RANGE_NOT_SATISFIABLE extends HttpStatus(416, "Requested Range Not Satisfiable")
  case object EXPECTATION_FAILED extends HttpStatus(417, "Expectation Failed")
  case object IM_A_TEAPOT extends HttpStatus(418, "I'm a teapot")
  case object ENHANCE_YOUR_CALM extends HttpStatus(420, "Enhance Your Calm")
  case object MISDIRECTED_REQUEST extends HttpStatus(421, "Misdirected Request")
  case object UNPROCESSABLE_ENTITY extends HttpStatus(422, "Unprocessable Entity")
  case object LOCKED extends HttpStatus(423, "Locked")
  case object FAILED_DEPENDENCY extends HttpStatus(424, "Failed Dependency")
  case object TOO_EARLY extends HttpStatus(425, "Too Early")
  case object UPGRADE_REQUIRED extends HttpStatus(426, "Upgrade Required")
  case object PRECONDITION_REQUIRED extends HttpStatus(428, "Precondition Required")
  case object TOO_MANY_REQUESTS extends HttpStatus(429, "Too Many Requests")
  case object REQUEST_HEADER_FIELDS_TOO_LARGE extends HttpStatus(431, "Request Header Fields Too Large")
  case object RETRY_WITH extends HttpStatus(449, "Retry With")
  case object BLOCKED_BY_PARENTAL_CONTROLS extends HttpStatus(450, "Blocked by Windows Parental Controls")
  case object UNAVAILABLE_FOR_LEGAL_REASONS extends HttpStatus(451, "Unavailable For Legal Reasons")
  case object INTERNAL_SERVER_ERROR extends HttpStatus(500, "Internal Server Error")
  case object NOT_IMPLEMENTED extends HttpStatus(501, "Not Implemented")
  case object BAD_GATEWAY extends HttpStatus(502, "Bad Gateway")
  case object SERVICE_UNAVAILABLE extends HttpStatus(503, "Service Unavailable")
  case object GATEWAY_TIMEOUT extends HttpStatus(504, "Gateway Timeout")
  case object HTTP_VERSION_NOT_SUPPORTED extends HttpStatus(505, "HTTP Version Not Supported")
  case object VARIANT_ALSO_NEGOTIATES extends HttpStatus(506, "Variant Also Negotiates")
  case object INSUFFICIENT_STORAGE extends HttpStatus(507, "Insufficient Storage")
  case object LOOP_DETECTED extends HttpStatus(508, "Loop Detected")
  case object BANDWIDTH_LIMIT_EXCEEDED extends HttpStatus(509, "Bandwidth Limit Exceeded")
  case object NOT_EXTENDED extends HttpStatus(510, "Not Extended")
  case object NETWORK_AUTHENTICATION_REQUIRED extends HttpStatus(511, "Network Authentication Required")
  case object NETWORK_READ_TIMEOUT extends HttpStatus(598, "Network read timeout error")
  case object NETWORK_CONNECT_TIMEOUT extends HttpStatus(599, "Network connect timeout error")
  case class UNRECOGNIZED_HTTP_STATUS private[web](override val code: Int) extends HttpStatus(code, "Unrecognized")

  def apply(code: Int): HttpStatus = code match {
    case 100 => CONTINUE
    case 101 => SWITCHING_PROTOCOLS
    case 102 => PROCESSING
    case 103 => EARLY_HINTS
    case 200 => OK
    case 201 => CREATED
    case 202 => ACCEPTED
    case 203 => NON_AUTHORITATIVE_INFORMATION
    case 204 => NO_CONTENT
    case 205 => RESET_CONTENT
    case 206 => PARTIAL_CONTENT
    case 207 => MULTI_HTTP_STATUS
    case 208 => ALREADY_REPORTED
    case 226 => IM_USED
    case 300 => MULTIPLE_CHOICES
    case 301 => MOVED_PERMANENTLY
    case 302 => FOUND
    case 303 => SEE_OTHER
    case 304 => NOT_MODIFIED
    case 305 => USE_PROXY
    case 307 => TEMPORARY_REDIRECT
    case 308 => PERMANENT_REDIRECT
    case 400 => BAD_REQUEST
    case 401 => UNAUTHORIZED
    case 402 => PAYMENT_REQUIRED
    case 403 => FORBIDDEN
    case 404 => NOT_FOUND
    case 405 => METHOD_NOT_ALLOWED
    case 406 => NOT_ACCEPTABLE
    case 407 => PROXY_AUTHENTICATION_REQUIRED
    case 408 => REQUEST_TIMEOUT
    case 409 => CONFLICT
    case 410 => GONE
    case 411 => LENGTH_REQUIRED
    case 412 => PRECONDITION_FAILED
    case 413 => REQUEST_ENTITY_TOO_LARGE
    case 414 => REQUEST_URI_TOO_LONG
    case 415 => UNSUPPORTED_MEDIA_TYPE
    case 416 => REQUESTED_RANGE_NOT_SATISFIABLE
    case 417 => EXPECTATION_FAILED
    case 418 => IM_A_TEAPOT
    case 420 => ENHANCE_YOUR_CALM
    case 421 => MISDIRECTED_REQUEST
    case 422 => UNPROCESSABLE_ENTITY
    case 423 => LOCKED
    case 424 => FAILED_DEPENDENCY
    case 425 => TOO_EARLY
    case 426 => UPGRADE_REQUIRED
    case 428 => PRECONDITION_REQUIRED
    case 429 => TOO_MANY_REQUESTS
    case 431 => REQUEST_HEADER_FIELDS_TOO_LARGE
    case 449 => RETRY_WITH
    case 450 => BLOCKED_BY_PARENTAL_CONTROLS
    case 451 => UNAVAILABLE_FOR_LEGAL_REASONS
    case 500 => INTERNAL_SERVER_ERROR
    case 501 => NOT_IMPLEMENTED
    case 502 => BAD_GATEWAY
    case 503 => SERVICE_UNAVAILABLE
    case 504 => GATEWAY_TIMEOUT
    case 505 => HTTP_VERSION_NOT_SUPPORTED
    case 506 => VARIANT_ALSO_NEGOTIATES
    case 507 => INSUFFICIENT_STORAGE
    case 508 => LOOP_DETECTED
    case 509 => BANDWIDTH_LIMIT_EXCEEDED
    case 510 => NOT_EXTENDED
    case 511 => NETWORK_AUTHENTICATION_REQUIRED
    case 598 => NETWORK_READ_TIMEOUT
    case 599 => NETWORK_CONNECT_TIMEOUT
    case x => UNRECOGNIZED_HTTP_STATUS(x)
  }
}