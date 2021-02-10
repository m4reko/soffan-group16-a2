package ciserver;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



/**
 * Skeleton of a ContinuousIntegrationServer which acts as Webhook. See the Jetty documentation for
 * API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    private static final String CONTEXT = "Group16CIServer";

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        HttpClient client = new HttpClient();

        Payload payload = new Payload(request.getReader());

        Repository repo = new Repository(payload);

        repo.cloneRemote();
        String buildOutput = repo.build();
        repo.parseBuild(buildOutput);
        repo.reportCommitStatus();
        repo.deleteRepository();
        try {
            repo.reportCommitStatus(client, CONTEXT);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

        response.getWriter().println("CI job done");
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
