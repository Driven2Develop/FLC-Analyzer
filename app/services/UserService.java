package services;

import com.google.inject.Inject;
import models.User;
import wsclient.MyWSClient;

import static helpers.JsonUtil.parseResultJsonNode;

/**
 * User layer class for processing user
 */
public class UserService {

    private MyWSClient myWSClient;
    private ProjectService projectService;

    private static String EMPLOYER_SEARCH_URL = "/users/0.1/users/";

    /**
     * Constructor for DI
     *
     * @param myWSClient myWSClient
     * @author Yvonne
     */
    @Inject
    public UserService(MyWSClient myWSClient, ProjectService projectService) {
        this.myWSClient = myWSClient;
        this.projectService = projectService;
    }

    /**
     * Get User by user ID<br>
     *
     * @param userId to search for
     * @return User
     * @author Yvonne Lee
     */
    public User findUserById(long userId) throws Exception {
        return parseResultJsonNode(this.myWSClient.initRequest(EMPLOYER_SEARCH_URL + "/" + userId).get(), User.class);
    }

    /**
     * Get User and Projects by user ID<br>
     *
     * @param userId user ID to retrieve user details and project list
     * @return User
     * @author Yvonne Lee
     */
    public User findUserAndProjectsById(long userId) throws Exception {
        User user = findUserById(userId);
        user.setProjects(projectService.findProjectsByOwnerId(userId));
        return user;
    }

}
