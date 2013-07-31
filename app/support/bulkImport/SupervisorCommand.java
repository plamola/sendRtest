package support.bulkImport;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 7/7/13
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupervisorCommand {

    public enum Status {START, PAUSE, RESUME, STOP, REPORT};

    private static Status status;

    public SupervisorCommand(Status stat) {
        this.status = stat;
    }

    public Status getStatus() {
        return status;
    }

}
