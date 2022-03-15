package integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.JsonBodyReadables;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.test.WithServer;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class HomeControllerTest extends WithServer implements JsonBodyReadables {

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String INDEX_URL = "/";
    private static final String JAVA = "Java";
    private static final String SEARCH_LATEST_TEN_JAVA_PROJECTS = "/project/" + JAVA;
    private static final String SEARCH_PROJECTS_BY_JAVA_ID = "/project/job/7";
    private static final int OK = 200;

    private AsyncHttpClient asyncHttpClient;
    private int port;

    @Before
    public void setUp() {
        asyncHttpClient = new DefaultAsyncHttpClient();
        port = this.testServer.getRunningHttpPort().getAsInt();
    }

    @After
    public void tearDown() throws IOException {
        asyncHttpClient.close();
    }

    @Test
    public void testIndex() throws Exception {
        String url = LOCAL_HOST + port + INDEX_URL;
        try (WSClient ws = play.test.WSTestClient.newClient(port)) {
            CompletionStage<WSResponse> stage = ws.url(url).get();
            WSResponse response = stage.toCompletableFuture().get();
            assertEquals(OK, response.getStatus());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchLatestTenProjects() throws Exception {
        String url = LOCAL_HOST + port + SEARCH_LATEST_TEN_JAVA_PROJECTS;
        try (WSClient ws = play.test.WSTestClient.newClient(port)) {
            WSResponse responseCompletionStage = ws.url(url).get().toCompletableFuture().get();
            String responseHtmlText = responseCompletionStage.getBody();
            assertThat(responseHtmlText).contains(JAVA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindProjectsByJobId() throws Exception {
        String url = LOCAL_HOST + port + SEARCH_PROJECTS_BY_JAVA_ID;
        try (WSClient ws = play.test.WSTestClient.newClient(port)) {
            WSResponse responseCompletionStage = ws.url(url).get().toCompletableFuture().get();
            String responseHtmlText = responseCompletionStage.getBody();
            assertThat(responseHtmlText).contains(JAVA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
