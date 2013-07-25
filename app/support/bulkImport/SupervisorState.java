package support.bulkImport;

import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 7/11/13
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupervisorState {

    private int succesCount = 0;
    private int failureCount = 0;
    private int timeOutcount = 0;

    private DateTime startTime = new DateTime();

    private int workers = 0;
    private int activeWorkers = 0;
    private int payloadCount = 0;

    private ImportSupervisorActor.Status status = ImportSupervisorActor.Status.STOPPED;

    private long transformerId;
    private String currentFile;
    private long nrOfLines;



    public long getNrOfLines() {
        return nrOfLines;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFileSpecs(String currentFile, long nrOfLines) {
        this.currentFile = currentFile;
        this.nrOfLines = nrOfLines;
    }


    public long getTransformerId() {
        return transformerId;
    }

    public void setTransformerId(long transformerId) {
        this.transformerId = transformerId;
    }

    public int getSuccesCount() {
        return succesCount;
    }

    public void incrementSuccesCount() {
        this.succesCount++;
        resetTimeOutCount();
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void incrementFailureCount() {
        this.failureCount++;
        resetTimeOutCount();
    }

    public int getTimeOutcount() {
        return timeOutcount;
    }

    public void resetTimeOutCount() {
        this.timeOutcount=0;
    }

    public void incrementTimeOutCount() {
        this.timeOutcount++;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public void setWorkers(int count) {
        this.workers = count;
    }

    public int getWorkers() {
        return workers;
    }


    public void incrementActiveWorkers() {
        activeWorkers++;
    }

    public void decrementActiveWorkers() {
        activeWorkers--;
    }


    public int getActiveWorkers() {
        return activeWorkers;
    }

    public int getPayloadCount() {
        return payloadCount;
    }

    public void incrementPayloadCount() {
        this.payloadCount++;
    }

    public synchronized ImportSupervisorActor.Status getStatus() {
        return status;
    }

    public synchronized void setStatus(ImportSupervisorActor.Status status) {
        this.status = status;
    }


}
