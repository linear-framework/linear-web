# linear-web
Web module of the **Linear Framework**.

## API
The Linear Web API is built on top of [Spark Java](http://sparkjava.com).

### Quick Demo
```scala
package com.example
import com.linearframework.web._

object App {
  def main(args: Array[String]): Unit = {
    Server.autoScan("com.example").start()
  }
}

object HelloController extends Controller {
  GET("/") { (request, response) =>
    "Hello!"
  }
}
```

### Server
The `Server` is the heart of it all.  It starts a web server by scanning for `Controller`s, `Filter`s, and other web components.

Auto-configuration requires that **All components (including `Controller`, `Filter`, `ExceptionHandler`s, etc.) must be TOP-LEVEL OBJECTS**.
Classes, traits, abstract classes, nested object, and other constructs are not supported.

`Server` has a number of configuration options available prior to calling `start()`:
```scala
import com.linearframework.web._

object App {
  def main(args: Array[String]): Unit = {  
    Server
       // scan the given package for web objects
      .autoScan("com.example")
    
       // (optional) makes the server aware of its host name;
       //            defaults to "localhost"
      .host("myapp.org")
      
       // (optional) changes the port that the server runs on;
       //            defaults to 4567
      .port(8080)
      
       // (optional) allows cross-origin resource requests from the given origin;
       //            defaults to blocking all CORS requests
      .corsOrigin("*")
      
       // (optional) enables (or disables) request logging;
       //            defaults to disabled
      .logRequests(enabled = true)
      
       // (optional) enables static file hosting one of two ways:
       //              - a relative path on the CLASSPATH
       //              - an absolute EXTERNAL path on the filesystem
       //            defaults to blocking static file hosting 
      .staticFiles(CLASSPATH, "public/")
      
       // (optional) enables SSL over HTTPS with the given keystore and truststore;
       //            defaults to standard HTTP
      .secure(
        keystoreFilePath = "/keystore.jks", 
        keystorePassword = "s3cret", 
        truststoreFilePath = "truststore.ca", 
        truststorePassword = "super_s3cret"
      )
      
      // (optional) adds any arbitrary object to the Server.
      //            objects added with register() are accessible to all server components using:
      //             - the[XYZ] - fetches the first XYZ object registered to the server
      //             - all[XYZ] - fetches a list of all XYZ objects registered to the server
      .register[Database](new Database())
      
       // starts the server
      .start()
  }
}
```

### Controllers and Endpoints
`Controller`s define the endpoints of your REST server.  Each endpoint is made of three pieces:

- a **method** (GET, PUT, POST, etc.)
- a **path** (/hello, /users/:name)
- a **handler** (request, response) => { result }

Multiple endpoints may be defined in each `Controller` object, and 
a `Server` may have multiple `Controller` objects.

```scala
import com.linearframework.web._

object UserController extends Controller {

  GET("/api/users/:userId") { (request, response) =>
    val userId = request.getPathParam("userId")
    val user = UserService.findUser(userId)
    user
  }
  
  POST("/api/users/:userId") { (request, response) =>
    ...
  }
  
  ...
  
}
```

Supported request methods are `GET`, `POST`, `PUT`, `DELETE`, `HEAD`, `TRACE`, `CONNECT`, and `OPTIONS`. 

The return value of each endpoint will automatically be placed in the body of the response.  For
more granular control over how the response is built, you can manipulate `response` directly.

Reminder: for auto-configuration to work, all `Controller` implementations must be top-level Scala objects.

#### Response Transformers
A `ResponseTransformer` handles generically converting the return value of an endpoint (e.g., converting the value to JSON).

`ResponseTransformer`s can either be applied to each invidual endpoint:

```scala
import com.linearframework.web._

object UserController extends Controller {
  private val toJson: ResponseTransformer = { (result, response) => 
    response.setContentType(JSON)
    new Gson().toJson(result)
  }

  GET("/api/user/:userId") { (request, response) =>
    val userId = request.getPathParam("userId")
    val user = UserService.findUser(userId)
    user
  } { toJson }
}
``` 

Or implicitly defined for all endpoints in a controller:

```scala
import com.linearframework.web._

object UserController extends Controller {
  implicit private val toJson: ResponseTransformer = { (result, response) => 
    response.setContentType(JSON)
    new Gson().toJson(result)
  }

  GET("/api/user/:userId") { (request, response) =>
    val userId = request.getPathParam("userId")
    val user = UserService.findUser(userId)
    user
  }
}
``` 

### Filters
`Filter`s contain logic that is applied to every request.  A filter is made up of three pieces:

- a **method** (BEFORE, AFTER, or AFTER_AFTER)
- a **path pattern** (/api/\*, /users/\*)
- a **handler** (request, response) => { }

`BEFORE` filters are executed before each request.  
`AFTER` filters are executed after each request.  
`AFTER-AFTER` filters are executed after `AFTER` filters, and can be thought of like a `finally` block.

Multiple filters may be defined in each `Filter` object, and 
a `Server` may have multiple `Filter` objects.

```scala
import com.linearframework.web._
object AuthFilter extends Filter {

  BEFORE("/api/*") { (request, response) =>
    val authToken = request.getHeader("Authorization")
    validateToken(authToken)
  }
  
  // when no path pattern is provided, the filter will run on all requests
  AFTER { (request, response) =>
    response.setHeader("X-App-Name", "Sample App")
  }
  
  ...
  
}
```

Reminder: for auto-configuration to work, all `Filter` implementations must be top-level Scala objects.


### Exception Handling
`ExceptionHandler`s provide a mechanism for intercepting exceptions before a response is sent to the client.

A `Server` may have multiple `ExceptionHandler` objects. One `ExceptionHandler` object must be defined 
for each type of exception you wish to handle.

```scala
import com.linearframework.web._

object IllegalAccessExceptionHandler extends ExceptionHandler[IllegalAccessException] {
  val exceptionClass: Class[IllegalAccessException] = classOf[IllegalAccessException]
  
  def apply(exception: IllegalAccessException, request: Request, response: Response): Unit = {
    response.setStatus(UNAUTHORIZED)
    response.setContentType(PLAIN_TEXT)
    response.setBody(exception.getMessage)
  }
}
```

The framework also provides a fallthrough exception handler, for any exceptions that are not caught by your custom
`ExceptionHandler` implementations.  To override the built-in fallthrough handler, create a new 
object that extends `ExceptionHandler[Exception]` and it will automatically be used.

Reminder: for auto-configuration to work, all `ExceptionHandler` implementations must be top-level Scala objects.


### 404 Handling
The `NotFoundHandler` provides a mechanism for handling requests to endpoints that do not exist in the `Server`.

A `Server` may have exactly one `NotFoundHandler`.

```scala
import com.linearframework.web._
object My404Handler extends NotFoundHandler {
  override def apply(request: Request, response: Response): Unit = {
    response.setStatus(NOT_FOUND)
    response.setContentType(HTML)
    response.setBody("<h1>It ain't here, bud.</h1>")
  }
}
```

Reminder: for auto-configuration to work, the `NotFoundHandler` implementation must be a top-level Scala object.


### Other Components
A `Component` is a generic object which is registered with the `Server`.
Any `Component` object has access to all objects registered with the `Server` when the `Server` was created.

```scala
import com.linearframework.web._
object UserRepository extends Component {
  private lazy val db: Database = the[Database]
  def findUserById(userId: String): Option[User] = {
    db.sql(...)
  }
}
```

Reminder: for auto-configuration to work, all `Component` implementations must be top-level Scala objects.