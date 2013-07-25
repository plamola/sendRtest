package support.bulkImport.workers;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import play.Logger;
import support.bulkImport.Payload;
import support.bulkImport.WorkerResult;

import static java.lang.Thread.sleep;


public abstract class AbstractWorkerActor extends UntypedActor {

    private ActorRef mySupervisor;

    public AbstractWorkerActor(ActorRef inJobController) {
        mySupervisor = inJobController;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        WorkerResult result = new WorkerResult();
        if (message instanceof Payload) {
            Payload payload = (Payload) message;
            processPayload(payload, result);
            mySupervisor.tell(result, getSelf());
        } else {
            Logger.debug("I do not know what you want me to do with this.");
            result.setResult("I do not know what you want me to do with this.");
            result.setStatus(WorkerResult.Status.FAILED);
            sender().tell(result, getSelf());
        }
    }

    protected abstract void processPayload(Payload payload, WorkerResult result );

    @Override
    public void preStart() {
        Logger.debug(self().toString() + " - Starting worker");
        mySupervisor.tell(new WorkerResult(WorkerResult.Status.READY), getSelf());
    }

    @Override
    public void postStop() {
        Logger.debug(self().toString() + " - Terminated worker ");
        mySupervisor.tell(new WorkerResult(WorkerResult.Status.SUICIDE), getSelf());
    }



}
