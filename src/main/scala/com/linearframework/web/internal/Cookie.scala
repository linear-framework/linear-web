package com.linearframework.web.internal

import com.linearframework.web.SameSite
import java.time.{LocalDateTime, OffsetDateTime, ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter

private[web] class Cookie (
  name: String,
  value: String,
  maxAge: Option[Int] = None,
  expires: Option[ZonedDateTime] = None,
  secure: Option[Boolean] = None,
  httpOnly: Option[Boolean] = None,
  sameSite: Option[SameSite] = None,
  domain: Option[String] = None,
  path: Option[String] = None
) {

  override def toString: String = {
    this.validateName()
    this.validateValue()
    this.validateDomain()
    this.validatePath()

    val parts = List(
      s"$name=${Option(value).getOrElse("")}",

      maxAge match {
        case Some(age) if age != 0 => s"Max-Age=$age"
        case None =>
          expires match {
            case Some(date) =>
              val formatted = DateTimeFormatter.RFC_1123_DATE_TIME.format(date)
              val parsed = OffsetDateTime.parse(formatted, DateTimeFormatter.RFC_1123_DATE_TIME)
              val utc = parsed.atZoneSameInstant(ZoneOffset.UTC)
              s"Expires=${utc.format(DateTimeFormatter.RFC_1123_DATE_TIME)}"
            case None =>
              ""
          }
      },

      secure match {
        case Some(true) => "Secure"
        case _ => ""
      },

      httpOnly match {
        case Some(true) => "HttpOnly"
        case _ => ""
      },

      sameSite match {
        case Some(level) => s"SameSite=$level"
        case None => ""
      },

      domain match {
        case Some(d) if d.nonEmpty => s"Domain=$d"
        case None => ""
      },

      path match {
        case Some(p) if p.nonEmpty => s"Path=$p"
        case None => ""
      }
    )

    parts.filter(_.nonEmpty).mkString("; ")
  }

  private def validateName(): Unit = {
    val isValid =
      if (name == null || name.isEmpty) {
        false
      }
      else {
        if (name.matches("(.*)[\\s()<>@,;:\\\\\"/\\[\\]?={}]+(.*)")) {
          false
        }
        else {
          name.toCharArray.toSet.forall(c => c >= 32 && c <= 126)
        }
      }

    if (!isValid) {
      throw new IllegalArgumentException(
        s"Illegal cookie name [$name]. " +
        "A cookie name can be any US-ASCII characters, except control characters, spaces, or tabs. " +
        "It also must not contain a separator character like the following: ( ) < > @ , ; : \\ \" / [ ] ? = { }"
      )
    }
  }

  private def validateValue(): Unit = {
    val isValid = {
      if (value == null) {
        true
      }
      else {
        val quoteTrimmed = value.replaceFirst("^[\"]", "").replaceFirst("[\"]$", "")
        if (quoteTrimmed.matches("(.*)[\\s\",;\\\\]+(.*)")) {
          false
        }
        else {
          quoteTrimmed.toCharArray.toSet.forall(c => c >= 32 && c <= 126)
        }
      }
    }

    if (!isValid) {
      throw new IllegalArgumentException(
        s"Illegal cookie value [$value]. " +
        "A cookie value can optionally be wrapped in double quotes and include any US-ASCII characters " +
        "excluding control characters, Whitespace, double quotes, comma, semicolon, and backslash."
      )
    }
  }

  private def validateDomain(): Unit = {
    val isValid =
      domain match {
        case None =>
          true
        case Some(d) =>
          d.toCharArray.toSet.forall(c => c >= 32 && c <= 126)
      }

    if (!isValid) {
      throw new IllegalArgumentException(
        s"Illegal cookie domain [${domain.getOrElse("")}]."
      )
    }
  }

  private def validatePath(): Unit = {
    val isValid =
      path match {
        case None =>
          true
        case Some(d) =>
          d.toCharArray.toSet.forall(c => c >= 32 && c <= 126)
      }

    if (!isValid) {
      throw new IllegalArgumentException(
        s"Illegal cookie path [${path.getOrElse("")}]."
      )
    }
  }

}
