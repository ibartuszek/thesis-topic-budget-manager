package hu.elte.bm.transactionservice.web.maincategory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainCategoryController {

    private final MainCategoryModelService mainCategoryModelService;

    public MainCategoryController(final MainCategoryModelService mainCategoryModelService) {
        this.mainCategoryModelService = mainCategoryModelService;
    }

    @RequestMapping(value = "/bm/mainCategories/findAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getSubCategories(@RequestBody final MainCategoryModelRequestContext context) {
        List<MainCategoryModel> mainCategoryModelList = mainCategoryModelService.findAll(context);
        return new ResponseEntity<>(mainCategoryModelList, HttpStatus.OK);
    }

    @RequestMapping(value = "/bm/mainCategories/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> createMainCategory(@RequestBody final MainCategoryModelRequestContext context) {
        MainCategoryModelResponse response;
        try {
            response = mainCategoryModelService.saveMainCategory(context.getMainCategoryModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/mainCategories/update", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Object> updateMainCategory(@RequestBody final MainCategoryModelRequestContext context) {
        MainCategoryModelResponse response;
        try {
            response = mainCategoryModelService.updateMainCategory(context.getMainCategoryModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private MainCategoryModelResponse createErrorResponse(final MainCategoryModelRequestContext context, final Exception e) {
        MainCategoryModelResponse response = new MainCategoryModelResponse();
        response.setMainCategoryModel(context.getMainCategoryModel());
        response.setSuccessful(false);
        response.setMessage(e.getMessage());
        return response;
    }

}
