package support;

import models.Transformer;

/**
 * Author: matthijs
 * Created on: 19 Apr 2014.
 */
public class RandomLines implements SoapSource {

    private long lineCount = 0L;
    private long maxLines = 0L;
    public RandomLines(Transformer transformer) {
        this.maxLines = 1000000L;
    }


    @java.lang.Override
    public long getNrOfLines() {
        return this.maxLines;
    }


    @java.lang.Override
    public String getCurrentFileName() {
        return "Dummy_" +this.maxLines;
    }

    @java.lang.Override
    public synchronized String getNextLine() {
        return this.lineCount++ + ",\"Dummy\"";
    }


}
