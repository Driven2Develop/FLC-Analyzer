package controllers;

import com.google.inject.Inject;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

/**
 * User controller for retrieving users
 */
public class UserController extends Controller {

    private UserService userService;

    /**
     * Constructor for DI
     *
     * @author Yvonne Lee
     */
    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets user by ID and presents result to view
     *
     * @param userId user ID
     * @return user User
     * @throws Exception exception
     * @author Yvonne Lee
     */
    public Result findUserById(long userId) throws Exception {
        User user = userService.findUserAndProjectsById(userId);
        return ok(views.html.user.render(user));
    }
}
