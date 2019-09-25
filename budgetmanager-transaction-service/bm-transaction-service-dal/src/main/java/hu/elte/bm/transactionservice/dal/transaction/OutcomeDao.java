package hu.elte.bm.transactionservice.dal.transaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.MainCategoryRepository;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryRepository;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Component
public class OutcomeDao {

    private final OutcomeRepository outcomeRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TransactionEntityTransformer transactionEntityTransformer;

    OutcomeDao(final OutcomeRepository outcomeRepository, final MainCategoryRepository mainCategoryRepository,
        final SubCategoryRepository subCategoryRepository, final TransactionEntityTransformer transactionEntityTransformer) {
        this.outcomeRepository = outcomeRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.transactionEntityTransformer = transactionEntityTransformer;
    }

    public List<Transaction> findAll(final LocalDate start, final LocalDate end, final TransactionContext context) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findAll(convertToDate(start), convertToDate(end), context.getUserId())
            .forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    public Optional<Transaction> findById(final Long id, final TransactionContext context) {
        return outcomeRepository.findByIdAndUserId(id, context.getUserId()).map(transactionEntityTransformer::transformToTransaction);
    }

    public List<Transaction> findByTitle(final String title, final TransactionContext context) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findByTitleAndUserId(title, context.getUserId()).forEach(entity ->
            transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Transactional
    public Optional<Transaction> save(final Transaction transaction, final TransactionContext context) {
        OutcomeEntity entityToSave = createOutcomeEntity(transaction, context.getUserId());
        OutcomeEntity response = outcomeRepository.save(entityToSave);
        return Optional.ofNullable(transactionEntityTransformer.transformToTransaction(response));
    }

    public Optional<Transaction> update(final Transaction transaction, final TransactionContext context) {
        return save(transaction, context);
    }

    @Transactional
    public void delete(final Transaction transaction, final TransactionContext context) {
        OutcomeEntity entityToDelete = createOutcomeEntity(transaction, context.getUserId());
        outcomeRepository.delete(entityToDelete);
    }

    private Date convertToDate(final LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private OutcomeEntity createOutcomeEntity(final Transaction transaction, final Long userId) {
        return transactionEntityTransformer.transformToOutcomeEntity(transaction, getMainCategoryEntityFromRepository(transaction, userId),
            getSubCategoryFromRepository(transaction, userId), userId);
    }

    private MainCategoryEntity getMainCategoryEntityFromRepository(final Transaction transaction, final Long userId) {
        return mainCategoryRepository.findByIdAndUserId(transaction.getMainCategory().getId(), userId).orElse(null);
    }

    private SubCategoryEntity getSubCategoryFromRepository(final Transaction transaction, final Long userId) {
        return transaction.getSubCategory() == null ? null
            : subCategoryRepository.findByIdAndUserId(transaction.getSubCategory().getId(), userId).orElse(null);
    }

}
