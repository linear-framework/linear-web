package com.linearframework.web.internal

import com.linearframework.web.StaticFileLocation

private[web] case class StaticFilesConfiguration(
  location: StaticFileLocation,
  path: String
)

