package hu.elte.bm.transactionservice.web.subcategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.category.SubCategoryService;
import hu.elte.bm.transactionservice.web.common.ContextTransformer;

@RestController
public class SubCategoryController {

    private final SubCategoryService subCategoryService;
    private final ContextTransformer transformer;

    @Value("${sub_category.sub_category_has_been_saved}")
    private String categoryHasBeenSaved;

    @Value("${sub_category.sub_category_has_been_updated}")
    private String categoryHasBeenUpdated;

    SubCategoryController(final SubCategoryService subCategoryService, final ContextTransformer transformer) {
        this.subCategoryService = subCategoryService;
        this.transformer = transformer;
    }

    @RequestMapping(value = "/bm/subCategories/findAll", method = RequestMethod.GET, produces = "application/json")
    public SubCategoryListResponse getSubCategories(@RequestParam final TransactionType type, @RequestParam final Long userId) {
        List<SubCategory> subCategoryList = subCategoryService.getSubCategoryList(transformer.transform(type, userId));
        return SubCategoryListResponse.createSuccessfulSubCategoryResponse(subCategoryList);
    }

    @RequestMapping(value = "/bm/subCategories/create", method = RequestMethod.POST, produces = "application/json")
    public SubCategoryResponse createSubCategory(@RequestBody final SubCategoryRequestContext context) {
        SubCategory subCategory = subCategoryService.save(context.getSubCategory(), transformer.transform(context));
        return SubCategoryResponse.createSuccessfulSubCategoryResponse(subCategory, categoryHasBeenSaved);
    }

    @RequestMapping(value = "/bm/subCategories/update", method = RequestMethod.PUT, produces = "application/json")
    public SubCategoryResponse updateSubCategory(@RequestBody final SubCategoryRequestContext context) {
        SubCategory subCategory = subCategoryService.update(context.getSubCategory(), transformer.transform(context));
        return SubCategoryResponse.createSuccessfulSubCategoryResponse(subCategory, categoryHasBeenUpdated);
    }

}
