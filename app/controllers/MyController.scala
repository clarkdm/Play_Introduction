import scala.concurrent.Future

import play.api.mvc.{ Action, Controller }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._

import reactivemongo.api.gridfs.{ // ReactiveMongo GridFS
DefaultFileToSave, FileToSave, GridFS, ReadFile
}

import play.modules.reactivemongo.{ MongoController, ReactiveMongoComponents }
import reactivemongo.play.json._

trait ApplicationGame extends Controller
  with MongoController with ReactiveMongoComponents {

  implicit def materializer: akka.stream.Materializer

  // gridFSBodyParser from `MongoController`
  import MongoController.readFileReads

  val fsParser = gridFSBodyParser(reactiveMongoApi.gridFS)

  def upload = Action.async(fsParser) { request =>
    // here is the future file!
    val futureFile: Future[ReadFile[JSONSerializationPack.type, JsValue]] =
      request.body.files.head.ref

    futureFile.map { file =>
      // do something
      Ok
    }.recover {
      case e: Throwable => InternalServerError(e.getMessage)
    }
  }
}