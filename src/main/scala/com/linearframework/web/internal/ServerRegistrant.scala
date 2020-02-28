package com.linearframework.web.internal

import com.linearframework.web.Server
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

private[web] trait ServerRegistrant {
  private var server: Server = _

  private[web] final def setServer(server: Server): Unit = {
    this.server = server
  }

  private[web] def register(): Unit = {
    // no-op for abstract overrides
  }

  /**
   * Fetches all objects that have been registered with the server
   * with the given class
   */
  protected def all[T <: AnyRef](implicit classTag: ClassTag[T]): List[T] = {
    if (server == null) {
      List()
    }
    else {
      val javaSet = server.registry.get(classTag.runtimeClass)
      if (javaSet == null) {
        List()
      }
      else {
        javaSet.asScala.toList.asInstanceOf[List[T]]
      }
    }
  }

  /**
   * Fetches the first object that has been registered with the server
   * with the given class.
   */
  protected def the[T <: AnyRef](implicit classTag: ClassTag[T]): T = {
    val list = all[T]
    if (list.size < 1) {
      throw new IllegalArgumentException(s"Failed to find [${classTag.runtimeClass.getSimpleName}] registered with server; no such object is registered")
    }
    list.head
  }
}

