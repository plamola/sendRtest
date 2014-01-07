package controllers

import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._


object ServerSendEvents extends Controller {

  /** Central hub for distributing chat messages */
  val (chatOut, chatChannel) = Concurrent.broadcast[JsValue]

  /** Enumeratee for filtering messages based on channel */
  def filter(channel: String) = Enumeratee.filter[JsValue] {
    json: JsValue => (json \ "channel").as[String] == channel
  }

  /** Controller action serving activity based on channel */
  def statusFeed(channel: String) = Action {
    Ok.stream(chatOut &> filter(channel)  &> Concurrent.buffer(20) &> EventSource()).as("text/event-stream")
  }

  def statusFeedAll() = Action {
    Ok.stream(chatOut  &> Concurrent.buffer(20) &> EventSource()).as("text/event-stream")
  }

}