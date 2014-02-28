package controllers

/**
 * Author: matthijs 
 * Created on: 27 Feb 2014.
 */
import play.api._
import play.api.mvc._
import models.Transformer
import play.api.libs.json.{Json, Writes, JsValue, JsNumber}
import scala.collection.JavaConverters._
import scala.collection.mutable


object JsonActions extends Controller {

  implicit val TransformerWrite = new Writes[Transformer] {
    def writes(transformer: Transformer): JsValue = {
      Json.obj(
        "id" -> JsNumber(transformer.id.toLong),
        "name" -> transformer.name,
        "tags" -> transformer.tags
      )
    }
  }

  def transformersToJson(transformers : mutable.Buffer[Transformer]) : JsValue = {
    Json.toJson(transformers)
  }

  def index = Action {
    val transformers = Transformer.all()
    Ok(Json.toJson(transformersToJson(transformers.asScala)))
  }

}
