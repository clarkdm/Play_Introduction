package controllers

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}
import javax.inject.Inject

import scala.concurrent. Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.Formats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._


import scala.collection.mutable.ListBuffer


class ApplicationGame @Inject()(val messagesApi: MessagesApi, val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with I18nSupport with MongoController with ReactiveMongoComponents {

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("games"))


  def createUser = Action.async {
    val user = User("john", "jmith", "hi")


    val futureResult = collection.flatMap(_.insert(user))

    futureResult.map(_ => Ok)
  }

  def createGame(game: Game) = {


    val futureResult = collection.flatMap(_.insert(game))


  }


  def findByUserName(userName: String) = Action.async {
    val cursor: Future[Cursor[User]] = collection.map {
      _.find(Json.obj("userName" -> userName)).
        cursor[User]
    }
    println(cursor)
    val futureUsersList: Future[List[User]] = cursor.flatMap(_.collect[List]())

    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }

//  def findByAllGames = {
//    // let's do our query
//
//    //    var x: List[Game] = List()
//
//
//    val cursor: Future[Cursor[Game]] = collection.map {
//      _.find(Json.obj()).cursor[Game]
//    }
////
////
//    val futureUsersList: Future[List[Game]] = cursor.flatMap(_.collect[List]())
//    futureUsersList.onComplete(result => Ok(result.get))
//    // everything's ok! Let's reply with the array
//    //    futureUsersList.map { games =>
//    ////      println(games)
//    //      x = games
//    //return games
//    //    }
//    //    println(x)
//    //x
//  }





  def games: Action[AnyContent] = Action.async { implicit request =>
    val cursor: Future[Cursor[Game]] = collection.map {
      _.find(Json.obj())
        //.sort(Json.obj("id" -> -1))
        .cursor[Game]
    }
    val futureUsersList: Future[List[Game]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { persons =>
      Ok(views.html.GamesList(persons))
    }
  }





//  def games = Action { implicit request =>
//    findByAllGames
//    val cursor: Future[Cursor[Game]] = collection.map {
//      _.find(Json.obj()).cursor[Game]
//    }
//
//
//    val futureUsersList: Future[List[Game]] = cursor.flatMap(_.collect[List]())
//    futureUsersList.onComplete(result => Ok(views.html.GamesList(result.get)))
//
//
//
//Ok("sssss")
//  }

  def addGames = Action { implicit request =>

    Ok(views.html.AddGame(Formats.createGameForm))

  }

  def addGameToCart = Action { implicit request =>

    var x: ListBuffer[Game] = ListBuffer()

    Ok(views.html.GamesList(x.toList))
  }

  def addGamePost = Action { implicit request =>

    val formValidationRerult = Formats.createGameForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.AddGame(formWithErrors))
    }, { game =>
      createGame(game)
      Redirect(routes.ApplicationGame.addGames)
    })
  }

  def register = Action { implicit request =>

    Ok(views.html.register(Formats.createUserForm))

  }

  def addUser = Action { implicit request =>

    val formValidationRerult = Formats.createUserForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.register(formWithErrors))
    }, { user =>

      Redirect(routes.ApplicationGame.register)
    })
  }

  def login = Action { implicit request =>

    val formValidationRerult = Login.createLoginForm.bindFromRequest

    formValidationRerult.fold({
      formWithErrors =>
        BadRequest(views.html.Login_P(formWithErrors))
    }, { login =>
      println(login + " #############################" + (login.user_name == "cd1") + " : " + (login.password == "1234") + "###################")
      if (login.user_name == "cd1" && login.password == "1234") {
        Redirect(routes.ApplicationGame.games).withSession("login" -> login.user_name)
      } else Redirect(routes.ApplicationGame.games).withSession("login" -> "")

    })
  }

  def addtocart(game: String) = Action { implicit request =>

    request.session.get("cart") == "hi"
    if (request.cookies.get("cart").isEmpty) {
//      Ok().withCookies(Cookie("cart", game))
    }
    else {
//      Ok().withCookies(Cookie("cart", (request.cookies.get("cart").head.value + "###" + game)))
    }
    Ok("hi")
  }

}
