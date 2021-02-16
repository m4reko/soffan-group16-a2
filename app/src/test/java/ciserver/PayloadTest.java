package ciserver;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PayloadTest {
    /**
     * Test the Payload constructor with dummy static data.
     * 
     * @throws IOException
     */
    @Test
    public void testConstructor() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-push-data.json"));
        Payload payload = new Payload(reader);

        assertEquals("https://github.com/m4reko/soffan-group16-a2.git", payload.getCloneUrl());
        assertEquals("refs/heads/server-test", payload.getRef());
        assertEquals("https://api.github.com/repos/m4reko/soffan-group16-a2/statuses/e6bc08391713050344d8e2a5160be560304badb6",
                payload.getStatusesUrl());
    }
}
