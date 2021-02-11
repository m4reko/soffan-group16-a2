package ciserver;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RepositoryTest {
    /**
     * Test the clone remote function by cloning the test branch of this project's repository
     * and checking if the readme file exist. It also test the get clone repository location
     * function as a bonus.
     * 
     * @throws IOException
     * @throws SecurityException
     */
    @Test
    public void testCloneRemote() throws IOException, SecurityException {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-simple-data.json"));
        Payload payload = new Payload(reader);

        Repository repository = new Repository(payload);
        String cloneRemoteStatus = repository.cloneRemote();

        assertEquals("Success", cloneRemoteStatus);

        String clonedRepositoryLocation = repository.getClonedRepositoryLocation();
        String readmeLocation = clonedRepositoryLocation + "/README.md";
        Path path = Paths.get(readmeLocation);

        assertEquals(true, Files.exists(path));

        // TODO Remove the created cloned repository automatically
    }
}
