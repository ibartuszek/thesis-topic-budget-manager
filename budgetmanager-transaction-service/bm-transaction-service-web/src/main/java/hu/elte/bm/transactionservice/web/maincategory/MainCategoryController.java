package hu.elte.bm.transactionservice.web.maincategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.category.MainCategoryService;
import hu.elte.bm.transactionservice.web.common.ContextTransformer;

@RestController
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;
    private final ContextTransformer transformer;

    @Value("${main_category.main_category_has_been_saved}")
    private String categoryHasBeenSaved;

    @Value("${main_category.main_category_has_been_updated}")
    private String categoryHasBeenUpdated;

    MainCategoryController(final MainCategoryService mainCategoryService, final ContextTransformer transformer) {
        this.mainCategoryService = mainCategoryService;
        this.transformer = transformer;
    }

    @RequestMapping(value = "/bm/mainCategories/findAll", method = RequestMethod.GET, produces = "application/json")
    public MainCategoryListResponse getSubCategories(@RequestParam final TransactionType type, @RequestParam final Long userId) {
        List<MainCategory> mainCategoryList = mainCategoryService.getMainCategoryList(transformer.transform(type, userId));
        return MainCategoryListResponse.createSuccessfulSubCategoryResponse(mainCategoryList);
    }

    @RequestMapping(value = "/bm/mainCategories/create", method = RequestMethod.POST, produces = "application/json")
    public MainCategoryResponse createMainCategory(@RequestBody final MainCategoryRequestContext context) {
        MainCategory mainCategory = mainCategoryService.save(context.getMainCategory(), transformer.transform(context));
        return MainCategoryResponse.createSuccessfulSubCategoryResponse(mainCategory, categoryHasBeenSaved);
    }

    @RequestMapping(value = "/bm/mainCategories/update", method = RequestMethod.PUT, produces = "application/json")
    public MainCategoryResponse updateMainCategory(@RequestBody final MainCategoryRequestContext context) {
        MainCategory mainCategory = mainCategoryService.update(context.getMainCategory(), transformer.transform(context));
        return MainCategoryResponse.createSuccessfulSubCategoryResponse(mainCategory, categoryHasBeenSaved);
    }

}
