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
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Util methods used for extracting JSON responses from server after an HTTP call
     *
     * @param resultJsonNode Raw type of response, to be parsed
     * @return List of Projects extracted from JSON response
     * @throws JsonProcessingException thrown if failed to process JSON into "Project" type
     * @author Megnqi Liu
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
}
