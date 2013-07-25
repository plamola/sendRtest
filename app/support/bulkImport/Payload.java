package support.bulkImport;

/**
 * Created with IntelliJ IDEA.
 * User: matthijs
 * Date: 6/30/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Payload {

    private String table;
    private String line;
    private long lineNumber;

    public Payload(String table, long lineNumber, String line) {
        this.table = table;
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public String getTable() {
        return table;
    }

    public String getLine() {
        return line;
    }


    public long getLineNumber() {
        return lineNumber;
    }


}
