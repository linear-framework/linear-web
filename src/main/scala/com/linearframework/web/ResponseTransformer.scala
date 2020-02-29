package com.linearframework.web

/**
 * Transforms a response.
 * Response Transformers could be used, for example, to generically convert responses to JSON.
 */
trait ResponseTransformer {

  /**
   * Applies a transformation to the given Response
   */
  def apply(body: AnyRef, response: Response): AnyRef

}
