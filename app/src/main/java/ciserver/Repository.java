package ciserver;

import java.io.IOException;
import java.io.File;

import org.apache.commons.lang3.NotImplementedException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class Repository {

    public enum CommitStatus {
        ERROR, PENDING, FAILURE, SUCCESS
    }

    private CommitStatus commitStatus;
    private Payload payload;

    public Repository(Payload payload) {
        throw new NotImplementedException();
    }

    private void cloneRemote() throws IOException{
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

    public void build() {
    }

    public void reportCommitStatus() {
        throw new NotImplementedException();
    }

}
