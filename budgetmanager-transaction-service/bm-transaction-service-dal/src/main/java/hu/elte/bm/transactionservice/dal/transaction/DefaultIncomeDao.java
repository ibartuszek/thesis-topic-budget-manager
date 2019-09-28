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
import hu.elte.bm.transactionservice.service.database.IncomeDao;

@Component
public class DefaultIncomeDao implements IncomeDao {

    private final IncomeRepository incomeRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TransactionEntityTransformer transactionEntityTransformer;

    DefaultIncomeDao(final IncomeRepository incomeRepository, final MainCategoryRepository mainCategoryRepository,
        final SubCategoryRepository subCategoryRepository,
        final TransactionEntityTransformer transactionEntityTransformer) {
        this.incomeRepository = incomeRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.transactionEntityTransformer = transactionEntityTransformer;
    }

    @Override
    public List<Transaction> findAll(final LocalDate start, final LocalDate end, final Long userId) {
        List<Transaction> transactionList = new ArrayList<>();
        incomeRepository.findAll(convertToDate(start), convertToDate(end), userId)
            .forEach(entity -> transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Override
    public Optional<Transaction> findById(final Long id, final Long userId) {
        return incomeRepository.findByIdAndUserId(id, userId)
            .map(transactionEntityTransformer::transformToTransaction);
    }

    @Override
    public List<Transaction> findByTitle(final String title, final Long userId) {
        List<Transaction> transactionList = new ArrayList<>();
        incomeRepository.findByTitleAndUserId(title, userId).forEach(entity ->
            transactionList.add(transactionEntityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Override
    @Transactional
    public Transaction save(final Transaction transaction, final Long userId) {
        IncomeEntity entityToSave = createIncomeEntity(transaction, userId);
        IncomeEntity response = incomeRepository.save(entityToSave);
        return transactionEntityTransformer.transformToTransaction(response);
    }

    @Override
    public Transaction update(final Transaction transaction, final Long userId) {
        return save(transaction, userId);
    }

    @Override
    @Transactional
    public Transaction delete(final Transaction transaction, final Long userId) {
        IncomeEntity entityToDelete = createIncomeEntity(transaction, userId);
        incomeRepository.delete(entityToDelete);
        return transaction;
    }

    private Date convertToDate(final LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private IncomeEntity createIncomeEntity(final Transaction transaction, final Long userId) {
        return transactionEntityTransformer.transformToIncomeEntity(transaction, getMainCategoryEntityFromRepository(transaction, userId),
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
