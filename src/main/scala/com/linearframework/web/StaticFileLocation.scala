package com.linearframework.web

/**
 * Represents the file system which hosts static files.
 */
sealed trait StaticFileLocation

/** Indicates that static files should be served from the classpath */
case object CLASSPATH extends StaticFileLocation

/** Indicates that static files should be served from the external file system */
case object EXTERNAL extends StaticFileLocation

