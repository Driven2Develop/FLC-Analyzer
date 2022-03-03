package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import models.Project;
import wsclient.MyWSClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectService {

    private MyWSClient myWSClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String PROJECT_SEARCH_URL = "/projects/0.1/projects/all/?offset=0&limit=10&sort_field=time_submitted&job_details=true";

    @Inject
    public ProjectService(MyWSClient myWSClient) {
        this.myWSClient = myWSClient;
    }

    private List<Project> parseResultJsonNode(JsonNode resultJsonNode) throws JsonProcessingException {
        ArrayNode projectsArrayNode = (ArrayNode) resultJsonNode.get("projects");
        Iterator<JsonNode> iterator = projectsArrayNode.elements();
        List<Project> projects = new ArrayList<>();
        while (iterator.hasNext()) {
            projects.add(mapper.treeToValue(iterator.next(), Project.class));
        }
        return projects;
    }

    public List<Project> searchLatestTenProjects(String searchTerms) throws Exception {
        return parseResultJsonNode(this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&query=" + searchTerms).get());
    }

    public List<Project> findProjectsByJobId(long jobId) throws Exception {
        return parseResultJsonNode(this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&jobs[]=" + jobId).get());
    }
}
