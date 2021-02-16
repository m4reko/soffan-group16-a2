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
    private JSONObject repoJson;

    /**
     * Takes a BufferedReader containing a Github Webhook json payload and extracts the useful
     * values.
     *
     * @param requestReader BufferedReader with JSON data
     */
    public Payload(BufferedReader requestReader) throws IOException {
        JSONObject json = parseJson(requestReader);
        this.repoJson = json.getJSONObject("repository");
        this.cloneUrl = this.repoJson.getString("clone_url");
        this.ref = json.getString("ref");
        this.statusesUrl = this.repoJson.getString("statuses_url");
    }

    /**
     * @return The URL needed to clone a repository
     */
    public String getCloneUrl() {
        return this.cloneUrl;
    }

    /**
     *
     * @return
     */
    public String getRef() {
        return this.ref;
    }

    /**
     * @return The url needed to write a status message
     */
    public String getStatusesUrl() {
        return this.statusesUrl;
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
