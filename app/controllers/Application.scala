package controllers

import javax.inject.Inject


import play.api._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def index_Cookie(cookie: String = "hi") = Action { implicit request =>
    Ok(views.html.index("Your new application is ready.")).withCookies(Cookie("test", cookie)).withSession("connected" -> "user@gmail.com")
  }


  def goo = Action { implicit request =>
    Redirect("https://www.google.co.uk")
  }

  def web(page: String) = Action { implicit request =>
    Redirect("https://www." + page + ".com")
  }


  def helpers(result: String) = Action { implicit request =>
    result match {
      case "OK" => Ok(views.html.status("OK" + request.session.get("connected").head))
      case "BadGateway" => BadGateway(views.html.status(result))
      case "GatewayTimeout" => GatewayTimeout(views.html.status(result))
      case "Created" => Created(views.html.status(result))
      case _ => NotFound(views.html.status(result))

    }

  }

  def todo = TODO


  def cookies = Action { implicit request =>

    Ok(views.html.status(request.cookies.get("test").head.value.toString()))

  }

  def no_cookies = Action { implicit request =>

    Ok(views.html.status(request.cookies.get("test").head.value.toString())).discardingCookies(DiscardingCookie("test"))
  }

  def listcd = Action { implicit request =>
    Ok(views.html.listCDs(CD.cds, CD.createCDForm))

  }

  def addcd = Action { implicit request =>

    val formValidationRerult = CD.createCDForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.listCDs(CD.cds, formWithErrors))
    }, { cd =>
      cd
      CD.cds.append(cd)
      Redirect(routes.Application.listcd())
    })
  }

}


