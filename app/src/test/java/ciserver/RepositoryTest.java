package ciserver;

import org.junit.Test;
import ciserver.Repository.CommitStatus;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.dynamic.HttpClientTransportDynamic;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;



public class RepositoryTest {
    /**
     * Test the clone remote function by cloning the test branch of this project's repository and
     * checking if the readme file exist. It also test the get clone repository location function as
     * a bonus.
     *
     * @throws IOException
     * @throws SecurityException
     */
    @Test
    public void testCloneRemote() throws IOException, SecurityException {
        BufferedReader reader =
                new BufferedReader(new FileReader("src/test/resources/test-push-data.json"));
        Payload payload = new Payload(reader);

        Repository repository = new Repository(payload);
        String cloneRemoteStatus = repository.cloneRemote();

        assertEquals("Success", cloneRemoteStatus);

        String clonedRepositoryLocation = repository.getClonedRepositoryLocation();
        String readmeLocation = clonedRepositoryLocation + "/README.md";
        Path path = Paths.get(readmeLocation);

        assertEquals(true, Files.exists(path));

        repository.deleteRepository();
    }

    /**
     * Test the delete repository function by cloning and then deleting the repository.
     *
     * @throws IOException
     * @throws SecurityException
     */
    @Test
    public void testDeleteRepository() throws IOException, SecurityException {
        BufferedReader reader =
                new BufferedReader(new FileReader("src/test/resources/test-push-data.json"));
        Payload payload = new Payload(reader);

        Repository repository = new Repository(payload);
        String cloneRemoteStatus = repository.cloneRemote();

        assertEquals("Success", cloneRemoteStatus);

        String clonedRepositoryLocation = repository.getClonedRepositoryLocation();
        String readmeLocation = clonedRepositoryLocation + "/README.md";
        Path path = Paths.get(readmeLocation);

        assertTrue(Files.exists(path));

        repository.deleteRepository();

        assertFalse(Files.exists(path));
    }

    /**
     * Test the build function. The mockOutput is an empty string as that is how a successful build
     * would output. This is because the build function runs gradlew build -p which only outputs
     * something if it fails.
     *
     * @throws IOException
     * @throws SecurityException
     * @throws InterruptedException
     */
    @Test
    public void testBuild() throws IOException, SecurityException, InterruptedException {
        BufferedReader reader =
                new BufferedReader(new FileReader("src/test/resources/test-push-data.json"));
        Payload payload = new Payload(reader);
        reader.close();

        Repository repository = new Repository(payload);

        repository.setClonedRepositoryLocation("src/test/resources/test-repo");

        assertEquals(0, repository.build());
    }


    /**
     * Test the report commit status functionality by reporting a fake commit status to a
     * server-test branch on the GitHub repo of this project. We check that we get the correct
     * response status code from GitHub.
     *
     * @throws Exception
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws ExecutionException
     */
    @Test
    public void testReportCommitStatus() throws Exception, IOException, InterruptedException,
            TimeoutException, ExecutionException {
        BufferedReader reader =
                new BufferedReader(new FileReader("src/test/resources/test-push-data.json"));

        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();

        ClientConnector clientConnector = new ClientConnector();
        clientConnector.setSslContextFactory(sslContextFactory);

        HttpClient client = new HttpClient(new HttpClientTransportDynamic(clientConnector));
        client.start();

        Payload payload = new Payload(reader);

        Repository repo = new Repository(payload);
        repo.setCommitStatus(CommitStatus.PENDING);

        assertEquals(201, repo.reportCommitStatus(client, "Group16CIServerTest"));
    }
}
