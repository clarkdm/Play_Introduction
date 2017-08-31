package models

import play.api.data.Forms._
import play.api.data._

import scala.collection.mutable.ArrayBuffer

case class CD(name: String, price: Int)

object CD {
  val createCDForm = Form(mapping("name" -> nonEmptyText, "price" -> number(min = 0, max = 222))(CD.apply)(CD.unapply))
  val cds = ArrayBuffer(CD("cd 1", 43), CD("cd 2", 122), CD("cd 3", 533))
}
