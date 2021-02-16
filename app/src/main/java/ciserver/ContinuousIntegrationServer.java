package ciserver;

import java.io.IOException;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.dynamic.HttpClientTransportDynamic;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
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
        try {
            HttpClient client = initHttpClient();
            Payload payload = new Payload(request.getReader());
            Repository repo = new Repository(payload);
            repo.reportCommitStatus(client, CONTEXT); // Reports pending
            repo.cloneRemote();
            int buildExitCode = repo.build();
            if (buildExitCode == 0) {
                System.out.println("Build successful");
            } else {
                System.out.println("Build failing");
            }
            repo.deleteRepository();
            repo.reportCommitStatus(client, CONTEXT); // Reports success or failure
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        response.getWriter().println("CI job done");
    }

    private static HttpClient initHttpClient() throws Exception {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        ClientConnector clientConnector = new ClientConnector();
        clientConnector.setSslContextFactory(sslContextFactory);
        HttpClient client = new HttpClient(new HttpClientTransportDynamic(clientConnector));
        client.start();
        return client;
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
