package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Project;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for JsonUtils
 *
 * @author Bowen Cheng
 */
public class JsonUtilTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String json = "{\"projects\": [{\"owner_id\": 25917774,\"time_submitted\": 1646889688,\"title\": \"revoked hireme project - 1646889627136\",\"type\": \"fixed\",\"preview_description\": \"projects resource test\",\"jobs\": [{\"id\": 3,\"name\": \"PHP\",\"category\": {\"id\": 1,\"name\": \"Websites, IT & Software\"},\"active_project_count\": null,\"seo_url\": \"php\",\"seo_info\": null,\"local\": false}]}]}";

    @Test
    public void testParseResultJsonNode() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        verify(JsonUtil.parseResultJsonNode(jsonNode));

    }

    @Test
    public void testParseResultJsonNode2() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        verify(JsonUtil.parseResultJsonNode(jsonNode, Project.class, "projects"));
    }

    @Test
    public void testParseResultJsonNode3() throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        assertNotNull(JsonUtil.parseResultJsonNode(jsonNode, Project.class));
    }

    private void verify(List<Project> results) {
        assertThat(results)
                .isNotEmpty()
                .hasSize(1)
                .extracting(Project::getOwnerId, Project::getPreviewDescription)
                .contains(tuple(25917774L, "projects resource test"));
    }
}