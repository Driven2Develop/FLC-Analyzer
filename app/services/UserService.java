package services;

import com.google.inject.Inject;
import models.User;
import wsclient.MyWSClient;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;

/**
 * User layer class for processing user
 *
 * @author Yvonne Lee
 */
public class UserService {

    private MyWSClient myWSClient;

    private static String USER_SEARCH_URL = "/users/0.1/users/%s?compact=true";

    private HashMap<Long, CompletionStage<User>> userByUserIdCache = new HashMap<>();

    /**
     * Constructor for DI
     *
     * @param myWSClient myWSClient
     * @author Yvonne
     */
    @Inject
    public UserService(MyWSClient myWSClient) {
        this.myWSClient = myWSClient;
    }

    /**
     * Get User by user ID<br>
     *
     * @param userId to search for
     * @return User
     * @author Yvonne Lee
     */
    public CompletionStage<User> findUserById(long userId) {
        return this.myWSClient.initRequest(String.format(USER_SEARCH_URL, userId)).getResults(userByUserIdCache, userId, User.class);
    }

}
