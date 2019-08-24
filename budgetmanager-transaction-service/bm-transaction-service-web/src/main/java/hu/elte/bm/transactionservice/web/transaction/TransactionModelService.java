package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionContext;
import hu.elte.bm.transactionservice.domain.transaction.TransactionService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service
public class TransactionModelService {

    private final ModelValidator validator;
    private final TransactionService transactionService;
    private final TransactionModelTransformer transformer;

    @Value("${transaction.user_id_cannot_be_null}")
    private String userIdCannotBeNull;

    @Value("${transaction.category_type_cannot_be_null}")
    private String categoryCannotBeNull;

    @Value("${transaction.transaction_is_invalid}")
    private String transactionIsInvalid;

    @Value("${transaction.transaction_has_been_saved}")
    private String transactionHasBeenSaved;

    @Value("${transaction.transaction_has_been_saved_before}")
    private String transactionHasBeenSavedBefore;

    @Value("${transaction.transaction_has_been_updated}")
    private String transactionHasBeenUpdated;

    @Value("${transaction.transaction_cannot_be_updated}")
    private String transactionCannotBeUpdated;

    @Value("${transaction.transaction_has_been_deleted}")
    private String transactionHasBeenDeleted;

    @Value("${transaction.transaction_cannot_be_deleted}")
    private String transactionCannotBeDeleted;

    TransactionModelService(final ModelValidator validator, final TransactionService transactionService,
        final TransactionModelTransformer transformer) {
        this.validator = validator;
        this.transactionService = transactionService;
        this.transformer = transformer;
    }

    List<TransactionModel> findAll(final TransactionModelRequestContext context) {
        validateTransactionContext(context.getUserId(), context.getTransactionType());
        TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
        List<Transaction> transactionList = transactionService.findAllTransaction(context.getStart(), context.getEnd(), transactionContext);
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        return transactionList.stream()
            .map(transaction -> transformer.transformToTransactionModel(transaction, firstPossibleDay))
            .collect(Collectors.toList());
    }

    TransactionModelResponse saveTransaction(final TransactionModelRequestContext context) {
        preValidateSavableTransaction(context);
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<Transaction> savedTransaction = transactionService.save(
                transformer.transformToTransaction(result.getTransactionModel()), transactionContext);
            updateResponse(savedTransaction, result, transactionHasBeenSaved, transactionHasBeenSavedBefore);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionModelResponse updateTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context);
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<Transaction> updatedTransaction = transactionService.update(
                transformer.transformToTransaction(result.getTransactionModel()), transactionContext);
            updateResponse(updatedTransaction, result, transactionHasBeenUpdated, transactionCannotBeUpdated);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionModelResponse deleteTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context);
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<Transaction> deletedTransaction = transactionService.delete(
                transformer.transformToTransaction(result.getTransactionModel()), transactionContext);
            updateResponse(deletedTransaction, result, transactionHasBeenDeleted, transactionCannotBeDeleted);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    private void preValidateSavableTransaction(final TransactionModelRequestContext context) {
        if (context.getTransactionModel().getId() != null) {
            throw new IllegalArgumentException(transactionIsInvalid);
        }
        validateTransactionModelFields(context);
    }

    private void preValidateUpdatableCategory(final TransactionModelRequestContext context) {
        Assert.notNull(context.getTransactionModel().getId(), transactionIsInvalid);
        validateTransactionModelFields(context);
    }

    private void validateTransactionModelFields(final TransactionModelRequestContext context) {
        validateTransactionContext(context.getUserId(), context.getTransactionType());
        Assert.notNull(context.getTransactionModel().getTitle(), transactionIsInvalid);
        Assert.notNull(context.getTransactionModel().getAmount(), transactionIsInvalid);
        Assert.notNull(context.getTransactionModel().getCurrency(), transactionIsInvalid);
        Assert.notNull(context.getTransactionModel().getTransactionType(), transactionIsInvalid);
        Assert.notNull(context.getTransactionModel().getMainCategory(), transactionIsInvalid);
        Assert.notNull(context.getTransactionModel().getDate(), transactionIsInvalid);
    }

    private void validateTransactionContext(final Long userId, final TransactionType transactionType) {
        Assert.notNull(userId, userIdCannotBeNull);
        Assert.notNull(transactionType, categoryCannotBeNull);
    }

    private TransactionModelResponse createResponseWithDefaultValues(final TransactionModelRequestContext context) {
        TransactionModel transactionModel = context.getTransactionModel();
        TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        transformer.populateValidationFields(transactionModel, firstPossibleDay);
        TransactionModelResponse result = new TransactionModelResponse();
        result.setTransactionModel(transactionModel);
        result.setFirstPossibleDay(firstPossibleDay);
        return result;
    }

    private boolean isValid(final TransactionModel transactionModel) {
        boolean compulsoryFields = validateCompulsoryFields(transactionModel);
        boolean endDate = transactionModel.getEndDate() == null || validator.validate(transactionModel.getEndDate(), "End date");
        boolean description = transactionModel.getDescription() == null || validator.validate(transactionModel.getDescription(), "Description");
        return compulsoryFields && endDate && description;
    }

    private boolean validateCompulsoryFields(final TransactionModel transactionModel) {
        boolean title = validator.validate(transactionModel.getTitle(), "Title");
        boolean amount = validator.validate(transactionModel.getAmount(), "Amount");
        boolean currency = validator.validate(transactionModel.getCurrency(), "Currency");
        boolean type = validator.validate(transactionModel.getTransactionType(), "Type");
        boolean date = validator.validate(transactionModel.getDate(), "Date");
        return title && amount && currency && type && date;
    }

    private void updateResponse(final Optional<Transaction> transaction,
        final TransactionModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (transaction.isPresent()) {
            response.setTransactionModel(transformer.transformToTransactionModel(transaction.get(), response.getFirstPossibleDay()));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(transactionType)
            .withUserId(userId)
            .build();
    }

}
