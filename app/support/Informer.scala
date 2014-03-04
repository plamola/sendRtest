package support

import support.bulkImport.SupervisorState
import play.api.libs.json.Json
import controllers.ServerSendEvents
import org.joda.time.DateTime

object Informer {

  def getInstance = this

  def sendMessage( status: SupervisorState, message: String) {
    val now: String = DateTime.now.toString("yyyy-MM-dd HH:mm:ss")
    val startTime = status.getStartTime.toString("yyyy-MM-dd HH:mm:ss")
    val stopTime = status.getStopTime.toString("yyyy-MM-dd HH:mm:ss")

    val msg = Json.obj(
      "channelId" -> status.getTransformerId,
      "channelName" -> status.getTransformerName,
      "successes" -> status.getSuccesCount,
      "failures" -> status.getFailureCount,
      "timeouts" -> status.getTimeOutcount,
      "activeworkers" -> status.getActiveWorkers,
      "starttime" -> status.getStartTime,
      "stoptime" -> status.getStopTime,
      "status" -> status.getStatus.toString,
      "text" ->  message,
      "currentFile" -> status.getCurrentFile,
      "nrOfLines" -> status.getNrOfLines,
      "startTime" -> startTime,
      "stopTime" -> stopTime,
      "time" -> now
    )
    ServerSendEvents.outputChannel.push(msg)
  }

}

