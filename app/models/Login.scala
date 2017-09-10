package models
import play.api.data.Forms._
import play.api.data._



case class Login(user_name: String, password: String)
object Login{
  val createLoginForm = Form(mapping("user_name" -> nonEmptyText, "password" -> nonEmptyText)(apply)(unapply))
}

