package wsclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import play.libs.ws.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static helpers.JsonUtil.parseResultJsonNode;

/**
 * Wrapper class for <code>WSClient</code>, providing methods to call Freelancer API asynchronously through http protocol
 *
 * @author Mengqi Liu
 */
public class MyWSClient implements WSBodyReadables, WSBodyWritables {

    private final WSClient ws;
    private WSRequest request;
    private static final String FREELANCER_SANDBOX_URL = "https://www.freelancer-sandbox.com/api";
    private static final String OAUTH_ACCESS_TOKEN = "JN7EzdE5iivghNOYapxJqjvKr8iEHP";
    private static final String OAUTH_HEADER_NAME = "freelancer-oauth-v1";
    private static final String JSON_FIELD_RESULT = "result";
    private static final String JSON_FIELD_STATUS = "status";
    private static final String JSON_FIELD_SUCCESS = "success";

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
     * initialize <code>WSRequest</code> object from <code>WSClient</code> with the target url as well as oauth access token for Freelancer website.
     *
     * @param apiUrl the API url defined by Freelancer API documentation
     * @return a wrapped WSClient instance with callable request
     * @author Mengqi Liu
     */
    public MyWSClient initRequest(String apiUrl) {
        this.request = ws.url(FREELANCER_SANDBOX_URL + apiUrl)
                .addHeader(OAUTH_HEADER_NAME, OAUTH_ACCESS_TOKEN);
        return this;
    }

    /**
     * call Freelancer API by http GET method asynchronously
     *
     * @return the json format result returned from Freelancer API
     * @author Mengqi Liu
     */
    public <T> CompletionStage<List<T>> getListResults(Class<T> classType, String jsonNodeField) {
        return this.request.get()
                .thenApplyAsync(r -> {
                    JsonNode responseJsonNode = getResponseNode(r);
                    if (isResponseSuccess(responseJsonNode)) {
                        try {
                            return parseResultJsonNode(responseJsonNode.findValue(JSON_FIELD_RESULT), classType, jsonNodeField);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    }
                    return null;
                });
    }

    /**
     * call Freelancer API by http GET method asynchronously
     *
     * @return the json format result returned from Freelancer API
     * @author Yvonne Lee
     */
    public <T> CompletionStage<T> getResults(Class<T> classType) {
        return this.request.get()
                .thenApplyAsync(r -> {
                    JsonNode responseJsonNode = getResponseNode(r);
                    if (isResponseSuccess(responseJsonNode)) {
                        try {
                            return parseResultJsonNode(getResponseValue(responseJsonNode), classType);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    }
                    return null;
                });
    }

    /**
     * Get response JSON node
     *
     * @param response WS response from the server
     * @return Entire response value
     *
     * @author Yvonne Lee
     */
    private JsonNode getResponseNode(WSResponse response) {
        return response.getBody(json());
    }

    /**
     * Check if response is success / not
     * @param responseJsonNode Response value in JsonNode
     * @return true: response is successful
     *
     * @author Yvonne Lee
     */
    private boolean isResponseSuccess(JsonNode responseJsonNode) {
        String status = responseJsonNode.findValue(JSON_FIELD_STATUS).textValue();
        return JSON_FIELD_SUCCESS.equalsIgnoreCase(status);
    }

    /**
     * Get response body from <code>result</code> field
     *
     * @param responseJsonNode
     * @return Response body in JsonNode
     */
    private JsonNode getResponseValue(JsonNode responseJsonNode) {
        return responseJsonNode.findValue(JSON_FIELD_RESULT);
    }
}
