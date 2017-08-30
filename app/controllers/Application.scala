package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action { implicitly: Request[AnyContent] =>
    Ok(views.html.index("Your new application is ready."))
  }

  def index_Cookie(cookie: String = "hi") = Action { implicitly: Request[AnyContent] =>
    Ok(views.html.index("Your new application is ready.")).withCookies(Cookie("test", cookie))
  }


  def goo = Action { implicitly: Request[AnyContent] =>
    Redirect("https://www.google.co.uk")
  }

  def web(page: String) = Action { implicitly: Request[AnyContent] =>
    Redirect("https://www." + page + ".com")
  }


  def helpers(result: String) = Action { implicitly: Request[AnyContent] =>
    result match {
      case "OK" => Ok(views.html.status("OK"))
      case "BadGateway" => BadGateway(views.html.status(result))
      case "GatewayTimeout" => GatewayTimeout(views.html.status(result))
      case "Created" => Created(views.html.status(result))
      case _ => NotFound(views.html.status(result))

    }

  }


  def cookies = Action { implicitly: Request[AnyContent] =>

    Ok(views.html.status(implicitly.cookies.get("test").head.value.toString()))//.discardingCookies(DiscardingCookie("test"))

  }
}


