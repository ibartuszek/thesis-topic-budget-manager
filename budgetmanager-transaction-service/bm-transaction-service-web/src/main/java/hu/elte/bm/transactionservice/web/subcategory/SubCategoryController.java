package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@RestController
public class SubCategoryController {

    private final SubCategoryModelService subCategoryModelService;

    public SubCategoryController(final SubCategoryModelService subCategoryModelService) {
        this.subCategoryModelService = subCategoryModelService;
    }

    @RequestMapping(value = "/bm/subCategories/findAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getSubCategories(@RequestParam final TransactionType type) {
        List<SubCategoryModel> subCategoryModelList = subCategoryModelService.findAll(type);
        return new ResponseEntity<>(subCategoryModelList, HttpStatus.OK);
    }

    @RequestMapping(value = "/bm/subCategories/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> createSubCategory(@RequestBody final SubCategoryModelRequestContext context) {
        SubCategoryModelResponse response;
        try {
            response = subCategoryModelService.saveSubCategory(context.getSubCategoryModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/bm/subCategories/update", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Object> updateSubCategory(@RequestBody final SubCategoryModelRequestContext context) {
        SubCategoryModelResponse response;
        try {
            response = subCategoryModelService.updateSubCategory(context.getSubCategoryModel());
        } catch (Exception e) {
            response = createErrorResponse(context, e);
        }
        return response.isSuccessful() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private SubCategoryModelResponse createErrorResponse(final SubCategoryModelRequestContext context, final Exception e) {
        SubCategoryModelResponse response = new SubCategoryModelResponse();
        response.setSubCategoryModel(context.getSubCategoryModel());
        response.setSuccessful(false);
        response.setMessage(e.getMessage());
        return response;
    }

}
