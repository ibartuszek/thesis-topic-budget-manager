package hu.elte.bm.authenticationservice.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final String APPLICATION_JSON = "application/json";
    private final UserModelService userModelService;

    public UserController(final UserModelService userModelService) {
        this.userModelService = userModelService;
    }

    @RequestMapping(value = "/bm/users/findById", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public ResponseEntity<Object> findUserById(@RequestParam final Long id) {
        UserModelResponse response;
        try {
            response = userModelService.findById(id);
        } catch (Exception e) {
            response = createErrorResponse(e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/users/findByEmail", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public ResponseEntity<Object> findUserByEmail(@RequestParam final String email) {
        UserModelResponse response;
        try {
            response = userModelService.findByEmail(email);
        } catch (Exception e) {
            response = createErrorResponse(e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/users/register", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ResponseEntity<Object> registerUser(@RequestBody final UserModelRequestContext context) {
        UserModelResponse response;
        try {
            response = userModelService.registerUser(context.getUserModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/users/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public ResponseEntity<Object> updateUser(@RequestBody final UserModelRequestContext context) {
        UserModelResponse response;
        try {
            response = userModelService.updateUser(context.getUserModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/users/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ResponseEntity<Object> deleteUser(@RequestBody final UserModelRequestContext context) {
        UserModelResponse response;
        try {
            response = userModelService.deleteUser(context.getUserModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private UserModelResponse createErrorResponse(final UserModelRequestContext context, final Exception e) {
        UserModelResponse response = new UserModelResponse();
        response.setUserModel(context.getUserModel());
        response.setSuccessful(false);
        response.setMessage(e.getMessage());
        return response;
    }

    private UserModelResponse createErrorResponse(final Exception e) {
        UserModelResponse response = new UserModelResponse();
        response.setSuccessful(false);
        response.setMessage(e.getMessage());
        return response;
    }

}
