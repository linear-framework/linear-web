package com.linearframework.web

class ResponseTransformerSpec extends RestSpec {
  override protected val conf: Server.Configuration = Server.autoScan("com.linearframework.json")

  "Response transformers" should "be applied implicitly" in {
    val result = get("http://localhost:4567/people")
    result.body should be ("""[{"name":"Steve","age":19},{"name":"Billy","age":23}]""")
    result.headers("Content-Type") should be ("application/json;charset=utf-8")
  }

}
