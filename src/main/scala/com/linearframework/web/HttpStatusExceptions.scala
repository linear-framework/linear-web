package com.linearframework.web

import HttpStatus._

class HttpStatusException(val status: HttpStatus, message: String, cause: Throwable = null) extends RuntimeException(message, cause)

class BadRequestException(message: String = BAD_REQUEST.reason, cause: Throwable = null) extends HttpStatusException(BAD_REQUEST, message, cause)

class UnauthorizedException(message: String = UNAUTHORIZED.reason, cause: Throwable = null) extends HttpStatusException(UNAUTHORIZED, message, cause)

class PaymentRequiredException(message: String = PAYMENT_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(PAYMENT_REQUIRED, message, cause)

class ForbiddenException(message: String = FORBIDDEN.reason, cause: Throwable = null) extends HttpStatusException(FORBIDDEN, message, cause)

class NotFoundException(message: String = NOT_FOUND.reason, cause: Throwable = null) extends HttpStatusException(NOT_FOUND, message, cause)

class MethodNotAllowedException(message: String = METHOD_NOT_ALLOWED.reason, cause: Throwable = null) extends HttpStatusException(METHOD_NOT_ALLOWED, message, cause)

class NotAcceptableException(message: String = NOT_ACCEPTABLE.reason, cause: Throwable = null) extends HttpStatusException(NOT_ACCEPTABLE, message, cause)

class ProxyAuthenticationRequiredException(message: String = PROXY_AUTHENTICATION_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(PROXY_AUTHENTICATION_REQUIRED, message, cause)

class RequestTimeoutException(message: String = REQUEST_TIMEOUT.reason, cause: Throwable = null) extends HttpStatusException(REQUEST_TIMEOUT, message, cause)

class ConflictException(message: String = CONFLICT.reason, cause: Throwable = null) extends HttpStatusException(CONFLICT, message, cause)

class GoneException(message: String = GONE.reason, cause: Throwable = null) extends HttpStatusException(GONE, message, cause)

class LengthRequiredException(message: String = LENGTH_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(LENGTH_REQUIRED, message, cause)

class PreconditionFailedException(message: String = PRECONDITION_FAILED.reason, cause: Throwable = null) extends HttpStatusException(PRECONDITION_FAILED, message, cause)

class RequestEntityTooLargeException(message: String = REQUEST_ENTITY_TOO_LARGE.reason, cause: Throwable = null) extends HttpStatusException(REQUEST_ENTITY_TOO_LARGE, message, cause)

class RequestUriTooLongException(message: String = REQUEST_URI_TOO_LONG.reason, cause: Throwable = null) extends HttpStatusException(REQUEST_URI_TOO_LONG, message, cause)

class UnsupportedMediaTypeException(message: String = UNSUPPORTED_MEDIA_TYPE.reason, cause: Throwable = null) extends HttpStatusException(UNSUPPORTED_MEDIA_TYPE, message, cause)

class RequestedRangeNotSatisfiableException(message: String = REQUESTED_RANGE_NOT_SATISFIABLE.reason, cause: Throwable = null) extends HttpStatusException(REQUESTED_RANGE_NOT_SATISFIABLE, message, cause)

class ExpectationFailedException(message: String = EXPECTATION_FAILED.reason, cause: Throwable = null) extends HttpStatusException(EXPECTATION_FAILED, message, cause)

class ImATeapotException(message: String = IM_A_TEAPOT.reason, cause: Throwable = null) extends HttpStatusException(IM_A_TEAPOT, message, cause)

class EnhanceYourCalmException(message: String = ENHANCE_YOUR_CALM.reason, cause: Throwable = null) extends HttpStatusException(ENHANCE_YOUR_CALM, message, cause)

class MisdirectedRequestException(message: String = MISDIRECTED_REQUEST.reason, cause: Throwable = null) extends HttpStatusException(MISDIRECTED_REQUEST, message, cause)

class UnprocessableEntityException(message: String = UNPROCESSABLE_ENTITY.reason, cause: Throwable = null) extends HttpStatusException(UNPROCESSABLE_ENTITY, message, cause)

class LockedException(message: String = LOCKED.reason, cause: Throwable = null) extends HttpStatusException(LOCKED, message, cause)

class FailedDependencyException(message: String = FAILED_DEPENDENCY.reason, cause: Throwable = null) extends HttpStatusException(FAILED_DEPENDENCY, message, cause)

class TooEarlyException(message: String = TOO_EARLY.reason, cause: Throwable = null) extends HttpStatusException(TOO_EARLY, message, cause)

class UpgradeRequiredException(message: String = UPGRADE_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(UPGRADE_REQUIRED, message, cause)

class PreconditionRequiredException(message: String = PRECONDITION_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(PRECONDITION_REQUIRED, message, cause)

class RequestHeaderFieldsTooLargeException(message: String = REQUEST_HEADER_FIELDS_TOO_LARGE.reason, cause: Throwable = null) extends HttpStatusException(REQUEST_HEADER_FIELDS_TOO_LARGE, message, cause)

class RetryWithException(message: String = RETRY_WITH.reason, cause: Throwable = null) extends HttpStatusException(RETRY_WITH, message, cause)

class BlockedByParentalControlsException(message: String = BLOCKED_BY_PARENTAL_CONTROLS.reason, cause: Throwable = null) extends HttpStatusException(BLOCKED_BY_PARENTAL_CONTROLS, message, cause)

class UnavailableForLegalReasonsException(message: String = UNAVAILABLE_FOR_LEGAL_REASONS.reason, cause: Throwable = null) extends HttpStatusException(UNAVAILABLE_FOR_LEGAL_REASONS, message, cause)

class InternalServerErrorException(message: String = INTERNAL_SERVER_ERROR.reason, cause: Throwable = null) extends HttpStatusException(INTERNAL_SERVER_ERROR, message, cause)

class NotImplementedException(message: String = NOT_IMPLEMENTED.reason, cause: Throwable = null) extends HttpStatusException(NOT_IMPLEMENTED, message, cause)

class BadGatewayException(message: String = BAD_GATEWAY.reason, cause: Throwable = null) extends HttpStatusException(BAD_GATEWAY, message, cause)

class ServiceUnavailableException(message: String = SERVICE_UNAVAILABLE.reason, cause: Throwable = null) extends HttpStatusException(SERVICE_UNAVAILABLE, message, cause)

class GatewayTimeoutException(message: String = GATEWAY_TIMEOUT.reason, cause: Throwable = null) extends HttpStatusException(GATEWAY_TIMEOUT, message, cause)

class HttpVersionNotSupportedException(message: String = HTTP_VERSION_NOT_SUPPORTED.reason, cause: Throwable = null) extends HttpStatusException(HTTP_VERSION_NOT_SUPPORTED, message, cause)

class VariantAlsoNegotiatesException(message: String = VARIANT_ALSO_NEGOTIATES.reason, cause: Throwable = null) extends HttpStatusException(VARIANT_ALSO_NEGOTIATES, message, cause)

class InsufficientStorageException(message: String = INSUFFICIENT_STORAGE.reason, cause: Throwable = null) extends HttpStatusException(INSUFFICIENT_STORAGE, message, cause)

class LoopDetectedException(message: String = LOOP_DETECTED.reason, cause: Throwable = null) extends HttpStatusException(LOOP_DETECTED, message, cause)

class BandwidthLimitExceededException(message: String = BANDWIDTH_LIMIT_EXCEEDED.reason, cause: Throwable = null) extends HttpStatusException(BANDWIDTH_LIMIT_EXCEEDED, message, cause)

class NotExtendedException(message: String = NOT_EXTENDED.reason, cause: Throwable = null) extends HttpStatusException(NOT_EXTENDED, message, cause)

class NetworkAuthenticationRequiredException(message: String = NETWORK_AUTHENTICATION_REQUIRED.reason, cause: Throwable = null) extends HttpStatusException(NETWORK_AUTHENTICATION_REQUIRED, message, cause)

class NetworkReadTimeoutException(message: String = NETWORK_READ_TIMEOUT.reason, cause: Throwable = null) extends HttpStatusException(NETWORK_READ_TIMEOUT, message, cause)

class NetworkConnectTimeoutException(message: String = NETWORK_CONNECT_TIMEOUT.reason, cause: Throwable = null) extends HttpStatusException(NETWORK_CONNECT_TIMEOUT, message, cause)
