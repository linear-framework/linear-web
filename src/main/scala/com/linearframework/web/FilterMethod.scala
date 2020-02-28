package com.linearframework.web

/**
 * Represents a method by which a filter may be applied
 */
sealed trait FilterMethod

/** Execute the filter BEFORE the request */
case object BEFORE extends FilterMethod

/** Execute the filter AFTER the request */
case object AFTER extends FilterMethod

/** Execute the filter AFTER all other AFTER filters have executed on the request */
case object AFTER_AFTER extends FilterMethod


