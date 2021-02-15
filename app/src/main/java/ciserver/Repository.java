package ciserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.StringRequestContent;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;


/**
 * Handles the repository functions such as to clone, build, and report back to Github about the
 * status of it.
 */
public class Repository {

    public enum CommitStatus {
        ERROR("error"), PENDING("pending"), FAILURE("failure"), SUCCESS("success");

        public final String value;

        private CommitStatus(String value) {
            this.value = value;
        }
    }

    private Payload payload;
    private CommitStatus commitStatus;
    private String clonedRepositoryLocation;

    /**
     * Constructs a repository object with a payload. Clones the corresponding remote repository.
     *
     * @param payload webhook payload
     */
    public Repository(Payload payload) throws IOException {
        this.payload = payload;
        this.commitStatus = CommitStatus.PENDING;
    }

    /**
     * Clones the remote repository that is sent in the payload.
     */
    public String cloneRemote() throws IOException {
        String cloneUrl = this.payload.getCloneUrl();
        String branch = this.payload.getRef();
        String uniqueID = UUID.randomUUID().toString();
        this.clonedRepositoryLocation = "src/cloneRemote-" + uniqueID;
        File file = new File(this.clonedRepositoryLocation);

        try (Git git = Git.cloneRepository().setURI(cloneUrl).setDirectory(file).setBranch(branch)
                .call()) {
            return "Success";
        } catch (GitAPIException e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    /**
     * Runs the Gradle build and checks to see if the build is successful or not.
     * It would set the commitStatus depending on the outcome of the build command.
     *
     * @return String the process output
     */
    public String build() {
        String buildCommand = this.clonedRepositoryLocation + "/gradlew build -q";
        Runtime runtime = Runtime.getRuntime();

        try {
            Process process = runtime.exec(buildCommand);
            process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String output = stringBuilder.toString();
            bufferedReader.close();
            process.destroy();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    /**
     * Parses the output of the build function.
     *
     * @return String the status of the output
     */
    public String parseBuild(String output) {
        if (output.equals("")) {
            this.commitStatus = CommitStatus.SUCCESS;
            return "Success";
        } else {
            this.commitStatus = CommitStatus.FAILURE;
            return "Fail";
        }
    }

    /**
     * Sends the commit status to GitHub using its REST API.
     *
     * @return The status code of the HTTP response. Should be 201 if sucessful.
     */
    public int reportCommitStatus(HttpClient client, String context)
            throws InterruptedException, TimeoutException, ExecutionException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("state", commitStatus.value);
        requestBody.put("context", context);
        Request request = client.POST(this.payload.getStatusesUrl());
        request.body(new StringRequestContent(requestBody.toString()));

        String token_data = System.getenv("CI_USER") + ":" + System.getenv("CI_TOKEN");
        final String token = Base64.getEncoder().encodeToString(token_data.getBytes(StandardCharsets.UTF_8));
        request.headers(headers -> headers.put(HttpHeader.AUTHORIZATION, "Basic " + token));

        Response response = request.send();
        System.err.println(response.getStatus());
        if (response.getStatus() != 201) {
            System.err.printf("reportCommitStatus response returned with status: %d %n",
                    response.getStatus());
        }
        return response.getStatus();
    }

    /**
     * Automatically deletes the repository that was cloned
     */
    public String deleteRepository() {
        File file = new File(this.clonedRepositoryLocation);
        try {
            FileUtils.deleteDirectory(file);
            return "Success";
        } catch(IOException e) {
            return "Failure";
        }
    }

    /**
     * Gets the path of the cloned repository
     *
     * @return String of the location
     */
    public String getClonedRepositoryLocation() {
        return this.clonedRepositoryLocation;
    }

    public void setCommitStatus(CommitStatus commitStatus) {
        this.commitStatus = commitStatus;
    }

}
