package ciserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.lang.Runtime;

import java.util.UUID;

import org.apache.commons.lang3.NotImplementedException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Handles the repository functions such as to clone, build, and 
 * report back to Github about the status of it.
 */
public class Repository {

    public enum CommitStatus {
        ERROR, PENDING, FAILURE, SUCCESS
    }

    private CommitStatus commitStatus;
    private Payload payload;
    private String clonedRepositoryLocation;

    /**
     * Constructs a repository object with a payload.
     * Clones the corresponding remote repository.
     *
     * @param payload webhook payload
     */
    public Repository(Payload payload) throws IOException {
        this.payload = payload;
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

        try (Git git = Git.cloneRepository()
            .setURI(cloneUrl)
            .setDirectory(file)
            .setBranch(branch)
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
     */
    public void build() {
        String buildCommand = "src/cloneRemote/gradlew build -q";
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
            System.out.println("done");
            String output = stringBuilder.toString();
            if (output.equals("")) {
                this.commitStatus = CommitStatus.SUCCESS;
            } else {
                this.commitStatus = CommitStatus.FAILURE;
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the commit status after building.
     * Uses REST API for Github or emails project members.
     */
    public void reportCommitStatus() {
        throw new NotImplementedException();
    }

    /**
     * Gets the path of the cloned repository
     * 
     * @return String of the location
     */
    public String getClonedRepositoryLocation() {
        return this.clonedRepositoryLocation;
    }

}
