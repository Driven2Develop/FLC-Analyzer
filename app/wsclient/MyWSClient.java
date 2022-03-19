package wsclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import play.libs.ws.*;

import java.util.HashMap;
import java.util.List;
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

    public void setRequest(WSRequest request) {
        this.request = request;
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
    public <U, T> CompletionStage<List<T>> getListResults(HashMap<U, CompletionStage<List<T>>> cache, U key, Class<T> classType, String jsonNodeField) {
        if(cache.containsKey(key)) {
            return cache.get(key);
        }
        CompletionStage<List<T>> results = this.request.get()
                .thenApplyAsync(r -> {
                    JsonNode responseJsonNode = r.getBody(json());
                    String status = responseJsonNode.findValue(JSON_FIELD_STATUS).textValue();
                    if (JSON_FIELD_SUCCESS.equalsIgnoreCase(status)) {
                        try {
                            return parseResultJsonNode(responseJsonNode.findValue(JSON_FIELD_RESULT), classType, jsonNodeField);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    }
                    return null;
                });
        cache.put(key, results);
        return results;

    }

    /**
     * call Freelancer API by http GET method asynchronously
     *
     * @return the json format result returned from Freelancer API
     * @author Yvonne Lee
     */
    public <U, T> CompletionStage<T> getResults(HashMap<U, CompletionStage<T>> cache, U key, Class<T> classType) {
        if(cache.containsKey(key)) {
            return cache.get(key);
        }
        CompletionStage<T> results = this.request.get()
                .thenApplyAsync(r -> {
                    JsonNode responseJsonNode = r.getBody(json());
                    String status = responseJsonNode.findValue(JSON_FIELD_STATUS).textValue();
                    if (JSON_FIELD_SUCCESS.equalsIgnoreCase(status)) {
                        try {
                            return parseResultJsonNode(responseJsonNode.findValue(JSON_FIELD_RESULT), classType);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    }
                    return null;
                });
        cache.put(key, results);
        return results;
    }
}
