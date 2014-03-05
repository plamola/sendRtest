package support.bulkImport.workers;

import akka.actor.ActorRef;
import models.Transformer;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.WS;
import support.bulkImport.Payload;
import support.bulkImport.SOAPCreator;
import support.bulkImport.WorkerResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebserviceWorkerActor extends AbstractWorkerActor {

    private final Transformer transformer;


    public WebserviceWorkerActor(ActorRef inJobController, Transformer transformer) {
        super(inJobController);
        this.transformer = transformer;
    }

    @Override
    protected void processPayload(Payload payload, WorkerResult result) {
        String soapBody;
        try {
            soapBody = tranformLineToSoapMessage(payload, transformer);
            if (soapBody == null) {
                result.setStatus(WorkerResult.Status.FAILED);
                return;
            }
        } catch (Exception e) {
            result.setResult(e.getMessage());
            result.setStatus(WorkerResult.Status.FAILED);
            return;
        }

        result.setLineNumber(payload.getLineNumber());
        try {
            WS.WSRequestHolder requestHolder =
                    WS.url(transformer.webserviceURL)
                            .setContentType("text/xml;charset=" + transformer.webserviceCharSet);
            requestHolder.setTimeout(transformer.webserviceTimeout);
            Logger.trace(self().toString() + " - Ready to send request to " + transformer.webserviceURL);
            WS.Response response = requestHolder.post(soapBody).get();

            if (response.getBody().indexOf("<soap:Fault>") > 0) {
                result.setFailedInput(payload.getLine());
                result.setResult("Failed: [line: " + payload.getLineNumber() + "] " + response.getStatus() + ": " + response.getBody());
                result.setStatus(WorkerResult.Status.FAILED);
            } else {
                result.setResult("Did: [line: " + payload.getLineNumber() + "] " + payload.getLine());
                result.setStatus(WorkerResult.Status.DONE);
            }
        } catch (Exception e) {
            // not a direct result of the parsed response; must try again?
            result.setFailedInput(payload.getLine());
            result.setResult("Failed: [line: " + payload.getLineNumber() + "] " + e.getMessage());
            result.setStatus(WorkerResult.Status.TIMEOUT);

        }
    }


    String tranformLineToSoapMessage(Payload payload, Transformer transformer) throws Exception {
        try {
            Map<String, String> values = parseLine(payload.getLine());

            values.put("user", transformer.webserviceUser);
            values.put("password", transformer.webservicePassword);
            values.put("timestamp", transformer.timeStampString);

            String bodyContent = transformer.webserviceTemplate;
            bodyContent = replaceValues(bodyContent, values);
            return bodyContent;
        } catch (Exception e) {
            Logger.error("Parsing line [" + payload.getLineNumber() + "] failed: " + e.getMessage());
            throw new Exception("Parsing line [" + payload.getLineNumber() + "] failed: " + e.getMessage());
        }
    }


    private static String replaceValues(final String template,
                                        final Map<String, String> values) {


        final StringBuffer sb = new StringBuffer();
        final Pattern pattern =
                Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            final String key = matcher.group(1);

            final String replacement = values.get(key);

            if (replacement == null) {
                throw new IllegalArgumentException(
                        "Template contains unmapped key: "
                                + key);
            }
            String withoutCtrlChars = replacement.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F]", "");
            if  (withoutCtrlChars != null && withoutCtrlChars.length() > 0) {
                final String field = "<![CDATA[" + withoutCtrlChars.replace("$", "\\$") + "]]>";
                matcher.appendReplacement(sb, field);
            } else {
                matcher.appendReplacement(sb, "");
            }

        }
        matcher.appendTail(sb);

        // Remove the empty elements from the XML.
        final String cleaned = SOAPCreator.translate(sb.toString());
        return cleaned;

    }


    /**
     * Parse a line into a list of fields This method can handle a separator or
     * quote inside a value
     *
     * @param line CSV line
     * @return Map of values
     * @throws Exception
     */
    Map<String, String> parseLine(String line) {
        Map<String, String> ar = new HashMap<String, String>();
        StringBuffer curVal = new StringBuffer();
        boolean inquotes = false;
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inquotes) {
                char QUOTE = '\"';
                if (ch == QUOTE) {
                    inquotes = false;
                } else {
                    curVal.append(ch);
                }
            } else {
                char DELIMITER = ',';
                char QUOTE = '\"';
                if (ch == QUOTE) {
                    inquotes = true;
                    if (curVal.length() > 0) {
                        // if this is the second quote in a value, add a quote
                        // this is for the double quote in the middle of a value
                        curVal.append(QUOTE);
                    }
                } else if (ch == DELIMITER) {
                    ar.put(String.valueOf(count), StringUtils.defaultString(curVal.toString()));
                    count++;
                    curVal = new StringBuffer();
                } else {
                    curVal.append(ch);
                }
            }
        }
        ar.put(String.valueOf(count), StringUtils.defaultString(curVal.toString()));
        return ar;
    }


}