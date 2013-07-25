package support.bulkImport.workers;

import akka.actor.ActorRef;
import play.Logger;
import support.bulkImport.Payload;
import support.bulkImport.WorkerResult;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 6/30/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DummyWorkerActor extends AbstractWorkerActor {

    private int payloadCount = 0;

    public DummyWorkerActor(ActorRef inJobController) {
        super(inJobController);
    }

    @Override
    protected void processPayload(Payload payload, WorkerResult result) {
        try {
            Random rand = new Random();
            sleep((rand.nextInt(200)+1)*100);
            result.setStatus(WorkerResult.Status.DONE);
        } catch (Exception e) {
            Logger.error("Dummy processor failed with error " + e.getMessage());
            result.setStatus(WorkerResult.Status.FAILED);
        }
    }

    private synchronized Payload getNextDummyPayload() {
        payloadCount++;
        if (payloadCount <= 100) {
            return new Payload("TABLE", payloadCount, "Package #" + payloadCount);
        } else {
            if (payloadCount == 101) {
                Logger.info("All lines queued, closing file");
            }
            return null;
        }
    }


}