package ciserver;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

/**
 * Extract and contains the relevant data from a GitHub Webhook push request.
 */
public class Payload {
    private String cloneUrl;
    private String ref;
    private String statusesUrl;
    private String sha;
    private JSONObject repoJson;

    /**
     * Takes a BufferedReader containing a Github Webhook json payload and extracts the useful
     * values.
     *
     * @param requestReader BufferedReader with JSON data
     * @throws IOException 
     */
    public Payload(BufferedReader requestReader) throws IOException {
        JSONObject payloadJson = parseJson(requestReader);
        this.repoJson = payloadJson.getJSONObject("repository");
        this.cloneUrl = this.repoJson.getString("clone_url");
        this.ref = payloadJson.getString("ref");
        this.sha = payloadJson.getString("after");
        this.statusesUrl = this.repoJson.getString("statuses_url");
    }

    /**
     * Gets the clone URL.
     * 
     * @return The URL needed to clone a repository
     */
    public String getCloneUrl() {
        return this.cloneUrl;
    }

    /**
     * Gets the ref of the push.
     * 
     * @return The ref of the pushed commit
     */
    public String getRef() {
        return this.ref;
    }

    /**
     * Gets the status URL.
     * 
     * @return The url needed to write a status message
     */
    public String getStatusesUrl() {
        return this.statusesUrl.replace("{sha}", this.sha);
    }

    /**
     * Parse a Reader into a JSONObject
     *
     * @param requestReader BufferedReader with JSON data
     * @return JSONobject with the data from the reader
     * @throws IOException
     */
    private static JSONObject parseJson(BufferedReader requestReader) throws IOException {
        return new JSONObject(requestReader.readLine());
    }
}
