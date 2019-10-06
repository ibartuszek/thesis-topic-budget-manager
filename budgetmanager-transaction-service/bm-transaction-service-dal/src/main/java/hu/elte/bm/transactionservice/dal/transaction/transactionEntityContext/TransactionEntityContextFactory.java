package hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.MainCategoryRepository;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryRepository;
import hu.elte.bm.transactionservice.dal.picture.PictureEntity;
import hu.elte.bm.transactionservice.dal.picture.PictureRepository;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.exceptions.PictureNotFoundException;
import hu.elte.bm.transactionservice.domain.exceptions.maincategory.MainCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.exceptions.subcategory.SubCategoryNotFoundException;
import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;

@Component
public class TransactionEntityContextFactory {

    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PictureRepository pictureRepository;

    @Value("${main_category.main_category_cannot_be_found:Original mainCategory cannot be found in the repository!}")
    private String mainCategoryNotFound;

    @Value("${sub_category.sub_category_cannot_be_found:Original subCategory cannot be found in the repository!}")
    private String subCategoryNotFound;

    @Value("${picture.picture_cannot_be_found:Original picture cannot be found in the repository!}")
    private String pictureNotFound;

    TransactionEntityContextFactory(final MainCategoryRepository mainCategoryRepository,
        final SubCategoryRepository subCategoryRepository, final PictureRepository pictureRepository) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.pictureRepository = pictureRepository;
    }

    public TransactionEntityContext create(final Transaction transaction, final Long userId) {
        return TransactionEntityContext.builder()
            .withTransaction(transaction)
            .withUserId(userId)
            .withMainCategoryEntity(getMainCategoryEntityFromRepository(transaction, userId))
            .withSubCategoryEntity(getSubCategoryFromRepository(transaction, userId))
            .withPictureEntity(getPictureEntityFromRepository(transaction, userId))
            .build();
    }

    private MainCategoryEntity getMainCategoryEntityFromRepository(final Transaction transaction, final Long userId) {
        MainCategoryEntity result = mainCategoryRepository.findByIdAndUserId(transaction.getMainCategory().getId(), userId)
            .orElse(null);
        validateMainCategory(transaction.getMainCategory(), result);
        return result;
    }

    private SubCategoryEntity getSubCategoryFromRepository(final Transaction transaction, final Long userId) {
        SubCategoryEntity result = null;
        if (transaction.getSubCategory() != null) {
            result = subCategoryRepository.findByIdAndUserId(transaction.getSubCategory().getId(), userId).orElse(null);
            validateSubCategory(transaction.getSubCategory(), result);
        }
        return result;
    }

    private PictureEntity getPictureEntityFromRepository(final Transaction transaction, final Long userId) {
        PictureEntity result = null;
        Picture picture = transaction.getPicture();
        if (picture != null) {
            if (picture.getId() != null) {
                result = pictureRepository.findByIdAndUserId(picture.getId(), userId).orElse(null);
                validatePicture(result, picture);
            } else {
                result = saveNewPictureEntity(userId, picture);
            }
        }
        return result;
    }

    private PictureEntity saveNewPictureEntity(final Long userId, final Picture picture) {
        PictureEntity result;
        PictureEntity toSave = PictureEntity.builder()
            .withPicture(picture.getPicture())
            .withUserId(userId)
            .build();
        result = pictureRepository.save(toSave);
        return result;
    }

    private void validateMainCategory(final MainCategory mainCategory, final MainCategoryEntity mainCategoryEntity) {
        if (mainCategoryEntity == null) {
            throw new MainCategoryNotFoundException(mainCategory, mainCategoryNotFound);
        }
    }

    private void validateSubCategory(final SubCategory subCategory, final SubCategoryEntity result) {
        if (result == null) {
            throw new SubCategoryNotFoundException(subCategory, subCategoryNotFound);
        }
    }

    private void validatePicture(final PictureEntity result, final Picture picture) {
        if (result == null) {
            throw new PictureNotFoundException(picture, pictureNotFound);
        }
    }

}
