package ciserver;

import org.apache.commons.lang3.NotImplementedException;


public class Repository {

    public enum CommitStatus {
        ERROR, PENDING, FAILURE, SUCCESS
    }

    private CommitStatus commitStatus;
    private Payload payload;

    public Repository(Payload payload) {
        throw new NotImplementedException();
    }

    private void cloneRemote(){
        throw new NotImplementedException();
    }

    public void build() {
        throw new NotImplementedException();
    }

    public void reportCommitStatus() {
        throw new NotImplementedException();
    }

}
