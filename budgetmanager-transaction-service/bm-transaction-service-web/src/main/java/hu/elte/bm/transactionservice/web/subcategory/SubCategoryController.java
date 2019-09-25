package hu.elte.bm.transactionservice.web.subcategory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.category.SubCategoryService;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@RestController
public class SubCategoryController {

    @Value("${sub_category.sub_category_has_been_saved}")
    private String categoryHasBeenSaved;

    @Value("${sub_category.sub_category_has_been_updated}")
    private String categoryHasBeenUpdated;

    private final SubCategoryService subCategoryService;

    public SubCategoryController(final SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @RequestMapping(value = "/bm/subCategories/findAll", method = RequestMethod.GET, produces = "application/json")
    public SubCategoryListResponse getSubCategories(@RequestParam final TransactionType type, @RequestParam final Long userId) {
        return SubCategoryListResponse.createSuccessfulSubCategoryResponse(subCategoryService.getSubCategoryList(createTransactionContext(type, userId)));
    }

    @RequestMapping(value = "/bm/subCategories/create", method = RequestMethod.POST, produces = "application/json")
    public SubCategoryResponse createSubCategory(@RequestBody final SubCategoryRequestContext context) {
        SubCategory subCategory = subCategoryService.save(context.getSubCategory(), createTransactionContext(context));
        return SubCategoryResponse.createSuccessfulSubCategoryResponse(subCategory, categoryHasBeenSaved);
    }

    @RequestMapping(value = "/bm/subCategories/update", method = RequestMethod.PUT, produces = "application/json")
    public SubCategoryResponse updateSubCategory(@RequestBody final SubCategoryRequestContext context) {
        SubCategory subCategory = subCategoryService.update(context.getSubCategory(), createTransactionContext(context));
        return SubCategoryResponse.createSuccessfulSubCategoryResponse(subCategory, categoryHasBeenUpdated);
    }

    private TransactionContext createTransactionContext(final SubCategoryRequestContext context) {
        return createTransactionContext(context.getTransactionType(), context.getUserId());
    }

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
                .withTransactionType(transactionType)
                .withUserId(userId)
                .build();
    }

}
