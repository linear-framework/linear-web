package com.linearframework.web.internal

private[web] case class SslConfiguration(
  keystoreFilePath: String,
  keystorePassword: String,
  truststoreFilePath: String,
  truststorePassword: String
)

