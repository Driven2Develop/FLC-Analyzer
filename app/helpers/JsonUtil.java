package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for parsing JSON responses
 * @author Mengqi Liu
 * @author Bowen Cheng
 * @author Yvonne Lee
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Util methods used for extracting JSON responses from server after an HTTP call
     *
     * @param resultJsonNode Raw type of response, to be parsed
     * @return List of Projects extracted from JSON response
     * @throws JsonProcessingException thrown if failed to process JSON into "Project" type
     * @author Mengqi Liu
     * @author Bowen Cheng
     */
    public static List<Project> parseResultJsonNode(JsonNode resultJsonNode) throws JsonProcessingException {
        ArrayNode projectsArrayNode = (ArrayNode) resultJsonNode.get("projects");
        Iterator<JsonNode> iterator = projectsArrayNode.elements();
        List<Project> projects = new ArrayList<>();
        while (iterator.hasNext()) {
            projects.add(MAPPER.treeToValue(iterator.next(), Project.class));
        }
        return projects;
    }

    /**
     * Util method used for extracting a list of objects from JSON response from server after an HTTP call
     *
     * @param resultJsonNode Raw type of response, to be parsed
     * @param classType Class to parse raw response to
     * @param jsonNodeField the field name in json
     * @return Generic list of objects extracted from JSON response
     * @throws JsonProcessingException thrown if failed to process JSON into given type
     * @author Mengqi Liu
     */
    public static <T> List<T> parseResultJsonNode(JsonNode resultJsonNode, Class<T> classType, String jsonNodeField) throws JsonProcessingException {
        ArrayNode projectsArrayNode = (ArrayNode) resultJsonNode.get(jsonNodeField);
        Iterator<JsonNode> iterator = projectsArrayNode.elements();
        List<T> objects = new ArrayList<>();
        while (iterator.hasNext()) {
            objects.add(MAPPER.treeToValue(iterator.next(), classType));
        }
        return objects;
    }

    /**
     * Util method used for extracting JSON response from server after an HTTP call
     *
     * @param resultJsonNode Raw type of response, to be parsed
     * @param classType Class to parse raw response to
     * @return Generic object extracted from JSON response
     * @throws JsonProcessingException thrown if failed to process JSON into given type
     * @author Yvonne Lee
     */
    public static <T> T parseResultJsonNode(JsonNode resultJsonNode, Class<T> classType) throws JsonProcessingException {
        return MAPPER.treeToValue(resultJsonNode, classType);
    }

}
