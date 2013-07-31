package support

import support.bulkImport.SupervisorState
import play.api.libs.json.Json
import controllers.ServerSendEvents
import org.joda.time.DateTime

object Informer {

  def getInstance = this

  def sendMessage( status: SupervisorState, message: String) {
    //val statusString = "My status = " + status.getStatus.toString
    val now: String = DateTime.now.toString("yyyy-MM-dd HH:mm:ss")
    val startTime = status.getStartTime.toString("yyyy-MM-dd HH:mm:ss")
    //val channelName = "channel" + "%01d".format(status.getTransformerId)

    val msg = Json.obj(
      "channelId" -> status.getTransformerId,
      "channelName" -> status.getTransformerName,
      "successes" -> status.getSuccesCount,
      "failures" -> status.getFailureCount,
      "timeouts" -> status.getTimeOutcount,
      "activeworkers" -> status.getActiveWorkers,
      "starttime" -> status.getStartTime,
      "status" -> status.getStatus.toString,
      "text" ->  message,
      "currentFile" -> status.getCurrentFile,
      "nrOfLines" -> status.getNrOfLines,
      "startTime" -> startTime,
      "time" -> now
    )
    ServerSendEvents.chatChannel.push(msg)
  }

}

