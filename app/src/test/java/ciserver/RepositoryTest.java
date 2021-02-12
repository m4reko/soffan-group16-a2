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
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-simple-data.json"));
        Payload payload = new Payload(reader);

        Repository repository = new Repository(payload);
        String cloneRemoteStatus = repository.cloneRemote();

        assertEquals("Success", cloneRemoteStatus);

        String clonedRepositoryLocation = repository.getClonedRepositoryLocation();
        String readmeLocation = clonedRepositoryLocation + "/README.md";
        Path path = Paths.get(readmeLocation);

        assertTrue(Files.exists(path));

        String deleteRepositoryStatus = repository.deleteRepository();

        assertFalse(Files.exists(path));
    }

    /**
     * Test the build function. The mockOutput is an empty string as that is how a successful build would output.
     * This is because the build function runs gradlew build -p which only outputs something if it fails.
     * 
     * @throws IOException
     * @throws SecurityException
     */
    @Test
    public void testBuild() throws IOException, SecurityException {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-simple-data.json"));
        Payload payload = new Payload(reader);
        reader.close();

        Repository repository = new Repository(payload);

        String mockOutput = "";

        String parseBuildOutput = repository.parseBuild(mockOutput);

        assertEquals("Success", parseBuildOutput);
    }
}
