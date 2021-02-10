package ciserver;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;



public class Payload {
    private String cloneUrl;
    private String ref;
    private String statusesUrl;

    public Payload(HttpServletRequest request) {
        throw new NotImplementedException();
    }

    public String getCloneUrl() {
        return this.cloneUrl;
    }

    public String getRef(){
        return this.ref;
    }

    public String getStatusesUrl() {
        return this.statusesUrl;
    }

    private static JSONObject parseJson(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = request.getReader();
        return new JSONObject(bufferedReader.readLine());
    }


}
