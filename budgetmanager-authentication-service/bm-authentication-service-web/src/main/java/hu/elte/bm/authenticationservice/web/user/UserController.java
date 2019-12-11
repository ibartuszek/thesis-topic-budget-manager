package hu.elte.bm.authenticationservice.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.service.UserService;

@RestController
public class UserController {

    private static final String APPLICATION_JSON = "application/json";
    private final UserService userService;

    @Value("${user.user_has_been_registered:User registration is completed!}")
    private String userHasBeenRegistered;

    @Value("${user.user_has_been_updated:User data has been updated!}")
    private String userHasBeenUpdated;

    @Value("${user.user_has_been_deleted:User has been deleted!}")
    private String userHasBeenDeleted;

    @Value("${user.logout.success:User has been logged out.}")
    private String userHasBeenLoggedOut;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/bm/users/findById", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public UserResponse findUserById(@RequestParam final Long id) {
        return UserResponse.createSuccessfulUserResponse(userService.findUserById(id), null);
    }

    @RequestMapping(value = "/bm/users/findByEmail", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public UserResponse findUserByEmail(@RequestParam final String email) {
        return UserResponse.createSuccessfulUserResponse(userService.findUserByEmail(email), null);
    }

    @RequestMapping(value = "/bm/users/register", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public UserResponse registerUser(@Valid @RequestBody final User user) {
        return UserResponse.createSuccessfulUserResponse(userService.registerUser(user), userHasBeenRegistered);
    }

    @RequestMapping(value = "/bm/users/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public UserResponse updateUser(@Valid @RequestBody final UserRequestContext context) {
        return UserResponse.createSuccessfulUserResponse(userService.updateUser(context.getUser()), userHasBeenUpdated);
    }

    @RequestMapping(value = "/bm/users/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public UserResponse deleteUser(@RequestBody final UserRequestContext context) {
        return UserResponse.createSuccessfulUserResponse(userService.deleteUser(context.getUser()), userHasBeenDeleted);
    }

    @RequestMapping(value = "/bm/users/logout", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public UserResponse logoutUser(@RequestParam final Long userId, HttpServletRequest request) {
        String invalidToken = request.getHeader("Authorization");
        return UserResponse.createSuccessfulUserResponse(userService.saveTokenIntoBlackList(userId, invalidToken), userHasBeenDeleted);
    }

}
