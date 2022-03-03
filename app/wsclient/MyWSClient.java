package wsclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

public class MyWSClient implements WSBodyReadables, WSBodyWritables {

    private final WSClient ws;
    private WSRequest request;
    private static final String FREELANCER_SANDBOX_URL = "https://www.freelancer-sandbox.com/api";
    private static final String OAUTH_ACCESS_TOKEN = "JN7EzdE5iivghNOYapxJqjvKr8iEHP";

    @Inject
    public MyWSClient(WSClient ws) {
        this.ws = ws;
    }

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

    public JsonNode get() throws Exception {
        JsonNode responseJsonNode = this.request.get().thenApply(r -> r.getBody(json())).toCompletableFuture().get();
        checkResponse(responseJsonNode);
        return responseJsonNode.findValue("result");
    }

    public JsonNode post(String body) throws Exception {
        JsonNode responseJsonNode = this.request.post(body).thenApply(r -> r.getBody(json())).toCompletableFuture().get();
        checkResponse(responseJsonNode);
        return responseJsonNode.findValue("result");
    }
}
