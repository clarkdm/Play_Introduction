package models

import play.api.data.Forms._
import play.api.data._


case class User(
                   userName: String,
                   name: String,
                   password: String)



case class Game(
                   id: Int,
                   name: String,
                   price: Int,
                   description: String)

object Formats {

  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  val createGameForm = Form(mapping("id" -> number, "name" -> nonEmptyText, "price" -> number(min = 0, max = 222), "description" -> text)(Game.apply)(Game.unapply))
  val createUserForm = Form(mapping("user_name" -> nonEmptyText, "password" -> nonEmptyText,"name" -> nonEmptyText)(User.apply)(User.unapply))

  implicit val GameDBFormat = Json.format[Game]
  implicit val UserDBFormat = Json.format[User]

}