package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionService;

@Service
@PropertySource("classpath:messages.properties")
public class TransactionModelService {

    private final ModelValidator validator;
    private final TransactionService transactionService;
    private final TransactionModelTransformer transformer;

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
        List<Transaction> transactionList = context.getEnd() == null
            ? transactionService.findAllTransaction(context.getStart(), context.getTransactionType())
            : transactionService.findAllTransaction(context.getStart(), context.getEnd(), context.getTransactionType());
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType());
        return transactionList.stream()
            .map(transaction -> transformer.transformToTransactionModel(transaction, firstPossibleDay))
            .collect(Collectors.toList());
    }

    TransactionModelResponse saveTransaction(final TransactionModelRequestContext context) {
        preValidateSavableTransaction(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            Optional<Transaction> savedTransaction = transactionService.save(transformer.transformToTransaction(result.getTransactionModel()));
            updateResponse(savedTransaction, result, transactionHasBeenSaved, transactionHasBeenSavedBefore);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionModelResponse updateTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            Optional<Transaction> updatedTransaction = transactionService
                .update(transformer.transformToTransaction(result.getTransactionModel()), context.getTransactionType());
            updateResponse(updatedTransaction, result, transactionHasBeenUpdated, transactionCannotBeUpdated);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionModelResponse deleteTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            Optional<Transaction> deletedTransaction = transactionService
                .delete(transformer.transformToTransaction(result.getTransactionModel()), context.getTransactionType());
            updateResponse(deletedTransaction, result, transactionHasBeenDeleted, transactionCannotBeDeleted);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    private void preValidateSavableTransaction(final TransactionModel transactionModel) {
        if (transactionModel.getId() != null) {
            throw new IllegalArgumentException(transactionIsInvalid);
        }
        validateTransactionModelFields(transactionModel);
    }

    private void preValidateUpdatableCategory(final TransactionModel transactionModel) {
        Assert.notNull(transactionModel.getId(), transactionIsInvalid);
        validateTransactionModelFields(transactionModel);
    }

    private void validateTransactionModelFields(final TransactionModel transactionModel) {
        Assert.notNull(transactionModel.getTitle(), transactionIsInvalid);
        Assert.notNull(transactionModel.getAmount(), transactionIsInvalid);
        Assert.notNull(transactionModel.getCurrency(), transactionIsInvalid);
        Assert.notNull(transactionModel.getTransactionType(), transactionIsInvalid);
        Assert.notNull(transactionModel.getMainCategory(), transactionIsInvalid);
        Assert.notNull(transactionModel.getDate(), transactionIsInvalid);
    }

    private TransactionModelResponse createResponseWithDefaultValues(final TransactionModelRequestContext context) {
        TransactionModel transactionModel = context.getTransactionModel();
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType());
        transformer.populateValidationFields(transactionModel, firstPossibleDay);
        TransactionModelResponse result = new TransactionModelResponse();
        result.setTransactionModel(transactionModel);
        result.setFirstPossibleDay(firstPossibleDay);
        return result;
    }

    private boolean isValid(final TransactionModel transactionModel) {
        boolean title = validator.validate(transactionModel.getTitle(), "Title");
        boolean amount = validator.validate(transactionModel.getAmount(), "Amount");
        boolean currency = validator.validate(transactionModel.getCurrency(), "Currency");
        boolean type = validator.validate(transactionModel.getTransactionType(), "Type");
        boolean date = validator.validate(transactionModel.getDate(), "Date");
        boolean endDate = transactionModel.getEndDate() == null || validator.validate(transactionModel.getEndDate(), "End date");
        boolean description = transactionModel.getDescription() == null || validator.validate(transactionModel.getDescription(), "Description");
        return title && amount && currency && type && date && endDate && description;
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

}
