package controllers


import akka.stream.scaladsl.FileIO
import akka.util.ByteString

import play.api.http.HttpEntity

import javax.inject.Inject


import models.CD
import play.api.i18n.{I18nSupport, MessagesApi}

import play.api.http.ContentTypes
import play.api.libs.Comet
import play.api.mvc._
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import akka.stream.scaladsl.Source


import scala.concurrent.duration._
import scala.language.postfixOps

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

  def rename_cookies(name1: String, name2: String) = Action { implicit request =>

    Ok(views.html.status("hi")).withCookies(Cookie(name2, request.cookies.get(name1).head.value.toString())).discardingCookies(DiscardingCookie(name1))
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
      CD.cds.append(cd)
      Redirect(routes.Application.listcd())
    })
  }

  def getPDF = Action { implicit request =>
    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Strict(ByteString("Hello world"), Some("text/plain"))
    )
  }

  def getPDF_2 = Action { implicit request =>

    Ok.sendFile(content = new java.io.File("./public/test_1.pdf"), fileName = _ => "test_1.pdf")
  }

  def getPDF_3 = Action { implicit request =>
    val file = new java.io.File("./public/test_2.pdf")
    val path = file.toPath
    val source = FileIO.fromPath(path)
    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Streamed(source, Some(file.length()), Some("application/pdf"))

    )
  }


  def clock() = Action {
    Ok(views.html.clock())
  }

  def clocklive() = Action { implicit request =>
    println("hi")
    Ok.chunked(stringSource via
      Comet.string("parent.clockChanged")).as(ContentTypes.HTML)
  }

  def stringSource: Source[String, _] = {

    val df: DateTimeFormatter = DateTimeFormatter.ofPattern("HH mm ss")
    val tickSource = Source.tick(0 millis, 100 millis, "TICK")
    val s = tickSource.map((tich) => df.format(ZonedDateTime.now()))
    s
  }

}


