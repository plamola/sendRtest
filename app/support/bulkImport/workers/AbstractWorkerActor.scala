package support.bulkImport.workers

import akka.actor.ActorRef
import akka.actor.UntypedActor
import play.Logger
import support.bulkImport.WorkerResult
import support.bulkImport.Payload

abstract class AbstractWorkerActor extends UntypedActor {

  private[workers] def this(inJobController: ActorRef) {
    this()
    mySupervisor = inJobController
  }

  override def onReceive(message: Any) {
    val result: WorkerResult = new WorkerResult
    if (message.isInstanceOf[Payload]) {
      val payload: Payload = message.asInstanceOf[Payload]
      processPayload(payload, result)
      mySupervisor.tell(result, getSelf())
    }
    else {
      Logger.debug("I do not know what you want me to do with this.")
      result.setResult("I do not know what you want me to do with this.")
      result.setStatus(WorkerResult.Status.FAILED)
      sender.tell(result, getSelf())
    }
  }

  protected def processPayload(payload: Payload, result: WorkerResult)

  override def preStart() {
    Logger.debug(self.toString + " - Starting worker")
    mySupervisor.tell(new WorkerResult(WorkerResult.Status.READY), getSelf())
  }

  override def postStop() {
    Logger.debug(self.toString + " - Terminated worker ")
    mySupervisor.tell(new WorkerResult(WorkerResult.Status.SUICIDE), getSelf())
  }

  private final var mySupervisor: ActorRef = null
}