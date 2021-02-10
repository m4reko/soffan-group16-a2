package ciserver;

import java.io.IOException;
import java.io.File;

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

    /**
     * Constructs a repository object with a payload.
     * Clones the corresponding remote repository.
     *
     * @param payload webhook payload
     */
    public Repository(Payload payload) throws IOException {
        this.payload = payload;
        cloneRemote();
    }

    /**
     * Clones the remote repository that is sent in the payload.
     */
    private void cloneRemote() throws IOException {
        String cloneUrl = this.payload.getCloneUrl();
        File file = new File("src/cloneRemote");

        try {
            Git git = Git.cloneRepository()
            .setURI(cloneUrl)
            .setDirectory(file)
            .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the Gradle build and checks to see if the build is successful or not.
     * It would set the commitStatus depending on the outcome of the build command.
     */
    public void build() {
    }

    /**
     * Sends the commit status after building.
     * Uses REST API for Github or emails project members.
     */
    public void reportCommitStatus() {
        throw new NotImplementedException();
    }

}
