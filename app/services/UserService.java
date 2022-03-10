package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.User;
import wsclient.MyWSClient;

public class UserService {

    private MyWSClient myWSClient;
    private ProjectService projectService;

    private static String EMPLOYER_SEARCH_URL = "/users/0.1/users/";
    private ObjectMapper mapper = new ObjectMapper();

    @Inject
    public UserService(MyWSClient myWSClient, ProjectService projectService) {
        this.myWSClient = myWSClient;
        this.projectService = projectService;
    }

    private User parseResultJsonNode(JsonNode resultJsonNode) throws JsonProcessingException {
        return mapper.treeToValue(resultJsonNode, User.class);
    }

    public User findUserById(long userId) throws Exception {
        User user =  parseResultJsonNode(this.myWSClient.initRequest(EMPLOYER_SEARCH_URL  + "/" + userId).get());
        user.setProjects(projectService.findProjectsByOwnerId(userId));
        return user;
    }

}
