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

    private static Map<String, ActorRef> map = new HashMap<String, ActorRef>();

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
        ActorRef supervisor = map.get(new Long(id).toString());
        return supervisor;
    }


    public void startImportManager(int workers, Transformer tr) {
        transformer = tr;
        final int wrks = workers;
        ActorRef supervisor = findSupervisor(tr.id);

        if (supervisor != null) {
            if(supervisor.isTerminated()) {
                Logger.debug("Supervisor found terminated");
                //supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.START));
            } else {
                supervisor.tell("Lets restart");
                supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.START));
                return;
            }
        }
        final ActorRef importManager = system.actorOf(
                new Props(new UntypedActorFactory() {
                    public UntypedActor create() {
                        return new ImportSupervisorActor(wrks, transformer);
                    }
                }), "importSupervisor_" + transformer.id );
        map.put(new Long(tr.id).toString(),importManager);
        Logger.info("Start import of " + transformer.importPath);
        //importManager.tell("First time to go");
    }


    public void stopImportManager(long id) {
        ActorRef supervisor = findSupervisor(id);
        if (supervisor != null) {
            //supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.STOP));
            supervisor.tell(PoisonPill.getInstance(), supervisor);
        }
    }


    public void pauseImportManager(long id) {
        ActorRef supervisor = findSupervisor(id);
        if (supervisor != null) {
            supervisor.tell(new SupervisorCommand(SupervisorCommand.Status.PAUSE));
            //supervisor.tell(PoisonPill.getInstance(), supervisor);
        }
    }

//    public void shutdown() {
//        // TODO Auto-generated method stub
//        Logger.info("Shutdown finished");
//
//    }
//
//    public void startup() {
//        // TODO Auto-generated method stub
//
//    }

}
