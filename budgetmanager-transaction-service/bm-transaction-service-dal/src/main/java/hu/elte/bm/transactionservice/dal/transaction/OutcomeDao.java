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

    public List<Transaction> findAll(final LocalDate start, final LocalDate end) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findAll(convertToDate(start), convertToDate(end))
            .forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    public Optional<Transaction> findById(final Long id) {
        Transaction transaction = outcomeRepository.findById(id).map(transactionEntityTransformer::transformToTransaction).orElse(null);
        return Optional.ofNullable(transaction);
    }

    public List<Transaction> findByTitle(final String title) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findByTitle(title).forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Transactional
    public Optional<Transaction> save(final Transaction transaction) {
        OutcomeEntity entityToSave = createOutcomeEntity(transaction);
        OutcomeEntity response = outcomeRepository.save(entityToSave);
        return Optional.ofNullable(transactionEntityTransformer.transformToTransaction(response));
    }

    public Optional<Transaction> update(final Transaction transaction) {
        return save(transaction);
    }

    @Transactional
    public void delete(final Transaction transaction) {
        OutcomeEntity entityToDelete = createOutcomeEntity(transaction);
        outcomeRepository.delete(entityToDelete);
    }

    private Date convertToDate(final LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private OutcomeEntity createOutcomeEntity(final Transaction transaction) {
        return transactionEntityTransformer.
            transformToOutcomeEntity(transaction, getMainCategoryEntityFromRepository(transaction), getSubCategoryFromRepository(transaction));
    }

    private MainCategoryEntity getMainCategoryEntityFromRepository(final Transaction transaction) {
        return mainCategoryRepository.findById(transaction.getMainCategory().getId()).orElse(null);
    }

    private SubCategoryEntity getSubCategoryFromRepository(final Transaction transaction) {
        return transaction.getSubCategory() == null ? null
            : subCategoryRepository.findById(transaction.getSubCategory().getId()).orElse(null);
    }

}
