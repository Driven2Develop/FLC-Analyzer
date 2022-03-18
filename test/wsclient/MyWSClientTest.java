package wsclient;

import models.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyWSClientTest {

    @InjectMocks
    private MyWSClient myWSClient;

    @Mock
    private WSClient wsClient;

    @Mock
    private WSRequest wsRequest;

    @Mock
    private WSResponse wsResponse;

    private static final String OAUTH_ACCESS_TOKEN = "any oauth access token";
    private static final String OAUTH_HEADER_NAME = "oauth header name";

    private static final String JSON_FIELD_PROJECTS = "project";

    /**
     * test <code>initRequest</code> method of class <code>MyWSClient</code>
     *
     * @author Mengqi Liu
     */
    @Test
    public void testInitRequest() {
        when(wsClient.url(any())).thenReturn(wsRequest);
        assertThat(myWSClient.initRequest(any())).isEqualTo(myWSClient);
    }

    /**
     * test <code>getListResults</code> method of class <code>MyWSClient</code>
     *
     * @author Mengqi Liu
     */
    @Test
    public void testGetListResults() {
        lenient().when(wsClient.url(any())).thenReturn(wsRequest);
        lenient().when(wsRequest.addHeader(OAUTH_HEADER_NAME, OAUTH_ACCESS_TOKEN)).thenReturn(wsRequest);
        myWSClient.setRequest(wsRequest);
        CompletionStage<WSResponse> futureWSResponse = CompletableFuture.completedStage(wsResponse);
        when(wsRequest.get()).thenReturn(futureWSResponse);
        assertThat(myWSClient.getListResults(new HashMap<>(), "cacheKey", Project.class, JSON_FIELD_PROJECTS)).isNotNull();
    }

    /**
     * test <code>getResults</code> method of class <code>MyWSClient</code>
     *
     * @author Yvonne Lee
     */
    @Test
    public void testGetResults() {
        lenient().when(wsClient.url(any())).thenReturn(wsRequest);
        lenient().when(wsRequest.addHeader(OAUTH_HEADER_NAME, OAUTH_ACCESS_TOKEN)).thenReturn(wsRequest);
        myWSClient.setRequest(wsRequest);
        CompletionStage<WSResponse> futureWSResponse = CompletableFuture.completedStage(wsResponse);
        when(wsRequest.get()).thenReturn(futureWSResponse);
        assertThat(myWSClient.getResults(new HashMap<>(), "cacheKey", Project.class)).isNotNull();
    }
}
