package support.bulkImport;

import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 6/29/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkerResult {

    public enum Status {DONE, FAILED, TIMEOUT, READY, SUICIDE};

    private String result;
    private String failedInput;
    private long lineNumber;
    private Status status;
    private DateTime start;
    private DateTime end;


    public String getFailedInput() {
        return failedInput;
    }

    public void setFailedInput(String failedInput) {
        this.failedInput = failedInput;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public WorkerResult() {
        this.start = new DateTime();
    }

    public WorkerResult(Status status) {
        this.status = status;
        this.start = new DateTime();
        this.end = this.start;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.end = new DateTime();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getProcessingTime() {
        return end.getMillis() - start.getMillis();
    }


}
