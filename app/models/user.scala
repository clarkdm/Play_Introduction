package models
import play.api.data.Forms._
import play.api.data._


import scala.collection.mutable.ArrayBuffer
case class User(user_name: String, password: String,name:String)
object User{
  val createUserForm = Form(mapping("user_name" -> nonEmptyText, "password" -> nonEmptyText,"name" -> nonEmptyText)(User.apply)(User.unapply))
  val users = ArrayBuffer(User("cd1", "hi","david"), User("cd2", "hi","david"), User("cd3", "hi","hi"))
}
