package controllers;

import com.google.inject.Inject;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

public class UserController extends Controller {

    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Result findUserById(long userId) throws Exception {
        User user = userService.findUserAndProjectsById(userId);
        return ok(views.html.user.render(user));
    }
}
