package support.bulkImport;

import akka.actor.*;
import models.Transformer;
import play.Logger;

import java.util.HashMap;
import java.util.Map;


public class ImportMangerSystem {

    private static ActorSystem system;

    private static ImportMangerSystem mySystem = null;

    private static Transformer transformer;

    private final static String SYSTEMNAME = "SendRContol";

    private static final Map<String, ActorRef> map = new HashMap<String, ActorRef>();

    public static ImportMangerSystem getInstance() {
        if (mySystem == null) {
            mySystem = new ImportMangerSystem();
            system = ActorSystem.create(SYSTEMNAME);
        }
        return mySystem;
    }


    private ImportMangerSystem() {
        // Private to prevent instantiation
    }


    private ActorRef findSupervisor(long id) {
        return map.get(Long.toString(id));
    }


    public void reportOnAllSuperVisors() {
        if (transformer == null) return;
        ActorRef supervisor = findSupervisor(transformer.id);
        for (ActorRef actor : map.values()) {
            actor.tell(new SupervisorCommand(SupervisorCommand.Status.REPORT), supervisor);
        }
    }


    public void startImportManager(int workers, Transformer tr) {
        transformer = tr;
        final int wrks = workers;
        ActorRef supervisor = findSupervisor(tr.id);

        if (supervisor != null) {
            if (supervisor.isTerminated()) {
                Logger.debug("Supervisor found terminated");
            } else {
                supervisor.tell("Lets restart", supervisor);
                supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.START), supervisor);
                return;
            }
        }
        final ActorRef importManager = system.actorOf(
                new Props(new UntypedActorFactory() {
                    public UntypedActor create() {
                        return new ImportSupervisorActor(wrks, transformer);
                    }
                }), "SupervisorFor_" + transformer.name);
        map.put(Long.toString(tr.id), importManager);
        Logger.info("Start test for " + transformer.name);
    }


    public void stopImportManager(long id) {
        ActorRef supervisor = findSupervisor(id);
        if (supervisor != null) {
            supervisor.tell(PoisonPill.getInstance(), supervisor);
        }
    }


    public void pauseImportManager(long id) {
        ActorRef supervisor = findSupervisor(id);
        if (supervisor != null) {
            supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.PAUSE), supervisor);
        }
    }


}
