package com.linearframework.web.internal

import com.linearframework.BaseSpec
import com.linearframework.web.SameSite
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CookieSpec extends BaseSpec {

  "Cookie objects" can "be converted to Set-Cookie strings" in {
    val now = ZonedDateTime.now()
    val nowStr = now.format(DateTimeFormatter.RFC_1123_DATE_TIME)

    new Cookie("name", "value").toString should be ("name=value")
    new Cookie("name", "value", maxAge = Some(60)).toString should be ("name=value; Max-Age=60")
    new Cookie("name", "value", expires = Some(now)).toString should be (s"name=value; Expires=$nowStr")
    new Cookie("name", "value", secured = Some(false)).toString should be (s"name=value")
    new Cookie("name", "value", secured = Some(true)).toString should be (s"name=value; Secured")
    new Cookie("name", "value", httpOnly = Some(false)).toString should be (s"name=value")
    new Cookie("name", "value", httpOnly = Some(true)).toString should be (s"name=value; HttpOnly")
    new Cookie("name", "value", sameSite = Some(SameSite.LAX)).toString should be (s"name=value; SameSite=Lax")
    new Cookie("name", "value", sameSite = Some(SameSite.STRICT)).toString should be (s"name=value; SameSite=Strict")
    new Cookie("name", "value", sameSite = Some(SameSite.NONE)).toString should be (s"name=value; SameSite=None")
    new Cookie("name", "value", domain = Some("example.com")).toString should be (s"name=value; Domain=example.com")
    new Cookie("name", "value", path = Some("/app/path")).toString should be (s"name=value; Path=/app/path")

    new Cookie(
      "name",
      "value",
      maxAge = Some(60),
      secured = Some(true),
      httpOnly = Some(true),
      sameSite = Some(SameSite.STRICT),
      domain = Some("example.com"),
      path = Some("/app/path")
    ).toString should be (s"name=value; Max-Age=60; Secured; HttpOnly; SameSite=Strict; Domain=example.com; Path=/app/path")
  }

  "If MaxAge and Expires are both provided, only MaxAge" should "be in the cookie" in {
    new Cookie("name", "value", maxAge = Some(60), expires = Some(ZonedDateTime.now())).toString should be ("name=value; Max-Age=60")
  }

  "Cookie name" must "be valid" in {
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = null, value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "", value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = " ", value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "  \t ", value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "cookie name", value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "cookie\\name", value = "value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "cookie[name]", value = "value").toString }

    new Cookie(name = "cookie_name", value = "value").toString should be ("cookie_name=value")
  }

  "Cookie value" must "be valid" in {
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "cookie value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "cookie\\value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "cookie;value").toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "\"\"cookie_value\"\"").toString }

    new Cookie(name = "name", value = "\"cookie_value\"").toString should be ("name=\"cookie_value\"")
  }

  "Cookie domain and path" should "contain valid characters" in {
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "value", domain = Some("example.ċom")).toString }
    an [IllegalArgumentException] should be thrownBy { new Cookie(name = "name", value = "value", path = Some("/app/ċ")).toString }
  }

}
