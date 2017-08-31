package controllers
import javax.inject.Inject

import models.{Game, Login, User}
import play.api._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class ApplicationGame @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {


  def games = Action { implicit request =>
    Ok(views.html.GamesList(Game.games))
  }

  def addGames = Action { implicit request =>

    Ok(views.html.AddGame(Game.createGameForm))

  }
  def addGameToCart = Action { implicit request =>

    Ok(views.html.GamesList(Game.games))

  }

  def addGamePost = Action { implicit request =>

    val formValidationRerult = Game.createGameForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.AddGame(formWithErrors))
    }, { game =>

      Game.games.append(game)
      Redirect(routes.ApplicationGame.addGames)
    })
  }
  def register = Action { implicit request =>

    Ok(views.html.register(User.createUserForm))

  }

  def addUser = Action { implicit request =>

    val formValidationRerult = User.createUserForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.register(formWithErrors))
    }, { user =>

      User.users.append(user)
      Redirect(routes.ApplicationGame.register)
    })
  }

  def login = Action { implicit request =>

    val formValidationRerult = Login.createLoginForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.Login_P(formWithErrors))
    }, { login =>
println(login+" #############################"+(login.user_name=="cd1")+" : "+(login.password=="1234")+"###################")
      if(login.user_name=="cd1"&&login.password=="1234"){
        Redirect(routes.ApplicationGame.games).withSession("login"-> login.user_name)
      } else Redirect(routes.ApplicationGame.games).withSession("login"-> "")
      //User.users.append(login)

    })
  }

def addtocart(game: String) = Action { implicit request =>

  request.session.get("cart")=="hi"
if (request.cookies.get("cart").isEmpty){Ok("hi").withCookies(Cookie("cart", game))}
  else {Ok("hicccc").withCookies(Cookie("cart", (request.cookies.get("cart").head.value+"###"+game)))}

}

}
