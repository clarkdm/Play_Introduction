package models

import play.api.data.Forms._
import play.api.data._

import scala.collection.mutable.ArrayBuffer

case class Game(name: String, price: Int,description:String)
object Game {
  val createGameForm = Form(mapping("name" -> nonEmptyText, "price" -> number(min = 0, max = 222),"description" -> text) (Game.apply)(Game.unapply))


  val games = ArrayBuffer(
    Game("Overwatch", 46,"The world needs heroes. Join over 30 million players* as you clash on the battlefields of tomorrow. Choose your hero from a diverse cast of soldiers, scientists, adventurers, and oddities. Bend time, defy physics, and unleash an array of extraordinary powers and weapons. Engage your enemies in iconic locations from around the globe in the ultimate team-based shooter. Take your place in Overwatch."),
    Game("Mass_Effect", 27,"Mass Effect: Andromeda takes players to the Andromeda galaxy, far beyond the Milky Way. There, players will lead our fight for a new home in hostile territory as the Pathfinder - a leader of military-trained explorers. This is the story of humanity’s next chapter, and player choices throughout the game will ultimately determine our survival."),
    Game("For_Honor", 24,"Carve a path of destruction through the battlefield in For Honor, a brand-new game developed by the renowned Ubisoft Montreal studio."),
    Game("For_Honor", 24,"Carve a path of destruction through the battlefield in For Honor, a brand-new game developed by the renowned Ubisoft Montreal studio."))
}