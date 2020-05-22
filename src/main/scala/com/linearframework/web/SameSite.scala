package com.linearframework.web

sealed abstract class SameSite(value: String) {
  override def toString: String = value
}

object SameSite {
  case object LAX extends SameSite("Lax")
  case object STRICT extends SameSite("Strict")
  case object NONE extends SameSite("None")

  def apply(str: String): SameSite = {
    str.trim.toUpperCase match {
      case "LAX" => LAX
      case "STRICT" => STRICT
      case "NONE" => NONE
      case _ => throw new IllegalArgumentException(s"Invalid SameSite option: $str")
    }
  }
}
