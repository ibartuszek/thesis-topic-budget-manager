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
public class IncomeDao {

    private final IncomeRepository incomeRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TransactionEntityTransformer transactionEntityTransformer;

    IncomeDao(final IncomeRepository incomeRepository, final MainCategoryRepository mainCategoryRepository, final SubCategoryRepository subCategoryRepository,
        final TransactionEntityTransformer transactionEntityTransformer) {
        this.incomeRepository = incomeRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.transactionEntityTransformer = transactionEntityTransformer;
    }

    public List<Transaction> findAll(final LocalDate start, final LocalDate end) {
        List<Transaction> transactionList = new ArrayList<>();
        incomeRepository.findAll(convertToDate(start), convertToDate(end))
            .forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    public Optional<Transaction> findById(final Long id) {
        Transaction transaction = incomeRepository.findById(id).map(transactionEntityTransformer::transformToTransaction).orElse(null);
        return Optional.ofNullable(transaction);
    }

    public List<Transaction> findByTitle(final String title) {
        List<Transaction> transactionList = new ArrayList<>();
        incomeRepository.findByTitle(title).forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Transactional
    public Optional<Transaction> save(final Transaction transaction) {
        IncomeEntity entityToSave = createIncomeEntity(transaction);
        IncomeEntity response = incomeRepository.save(entityToSave);
        return Optional.ofNullable(transactionEntityTransformer.transformToTransaction(response));
    }

    public Optional<Transaction> update(final Transaction transaction) {
        return save(transaction);
    }

    @Transactional
    public void delete(final Transaction transaction) {
        IncomeEntity entityToDelete = createIncomeEntity(transaction);
        incomeRepository.delete(entityToDelete);
    }

    private Date convertToDate(final LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private IncomeEntity createIncomeEntity(final Transaction transaction) {
        return transactionEntityTransformer
            .transformToIncomeEntity(transaction, getMainCategoryEntityFromRepository(transaction), getSubCategoryFromRepository(transaction));
    }

    private MainCategoryEntity getMainCategoryEntityFromRepository(final Transaction transaction) {
        return mainCategoryRepository.findById(transaction.getMainCategory().getId()).orElse(null);
    }

    private SubCategoryEntity getSubCategoryFromRepository(final Transaction transaction) {
        return transaction.getSubCategory() == null ? null
            : subCategoryRepository.findById(transaction.getSubCategory().getId()).orElse(null);
    }

}
