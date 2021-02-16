package ciserver;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PayloadTest {
    @Test
    public void testConstructor() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-data.json"));
        Payload payload = new Payload(reader);

        assertEquals("https://github.com/Codertocat/Hello-World.git", payload.getCloneUrl());
        assertEquals("refs/tags/simple-tag", payload.getRef());
        assertEquals("https://api.github.com/repos/Codertocat/Hello-World/statuses/{sha}",
                payload.getStatusesUrl());
    }
}
