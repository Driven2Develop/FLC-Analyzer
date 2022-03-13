package wsclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

/**
 * Wrapper class for WSClient, providing methods to call Freelancer API asynchronously through http protocol<br/>
 *
 * @author Mengqi Liu
 */
public class MyWSClient implements WSBodyReadables, WSBodyWritables {

    private final WSClient ws;
    private WSRequest request;
    private static final String FREELANCER_SANDBOX_URL = "https://www.freelancer-sandbox.com/api";
    private static final String OAUTH_ACCESS_TOKEN = "JN7EzdE5iivghNOYapxJqjvKr8iEHP";

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public MyWSClient(WSClient ws) {
        this.ws = ws;
    }

    /**
     * initialize WSRequest object from WSClient with the target url as well as oauth access token for Freelancer website.
     *
     * @param apiUrl the API url defined by Freelancer API documentation
     * @return a wrapped WSClient instance with callable request
     * @author Mengqi Liu
     */
    public MyWSClient initRequest(String apiUrl) {
        this.request = ws.url(FREELANCER_SANDBOX_URL + apiUrl)
                .addHeader("freelancer-oauth-v1", OAUTH_ACCESS_TOKEN);
        return this;
    }

    private void checkResponse(JsonNode responseJsonNode) throws Exception {
        String status = responseJsonNode.findValue("status").textValue();
        if (!"success".equalsIgnoreCase(status)) {
            throw new Exception("http error");
        }
    }

    /**
     * call Freelancer API by http GET method asynchronously
     *
     * @return the json format result returned from Freelancer API
     * @author Mengqi Liu
     */
    public JsonNode get() throws Exception {
        JsonNode responseJsonNode = this.request.get().thenApply(r -> r.getBody(json())).toCompletableFuture().get();
        checkResponse(responseJsonNode);
        return responseJsonNode.findValue("result");
    }
}
