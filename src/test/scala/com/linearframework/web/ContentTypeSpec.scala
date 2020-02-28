package com.linearframework.web

import com.linearframework.BaseSpec

class ContentTypeSpec extends BaseSpec {
  import ContentType._

  "Content Types" should "be convertible to case objects" in {
    ContentType("application/json") should be (JSON)
    JSON.toString should be ("application/json; charset=utf-8")

    ContentType("application/custom; charset=windows") should be (Custom("application", "custom"))
    Custom("application", "custom").toString should be ("application/custom")
  }

}
