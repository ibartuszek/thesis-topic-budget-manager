package hu.elte.bm.transactionservice.dal.transaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContext;
import hu.elte.bm.transactionservice.dal.transaction.transactionEntityContext.TransactionEntityContextFactory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.OutcomeDao;

@Component
public class DefaultOutcomeDao implements OutcomeDao {

    private final OutcomeRepository outcomeRepository;
    private final TransactionEntityContextFactory contextFactory;
    private final TransactionEntityTransformer entityTransformer;

    DefaultOutcomeDao(final OutcomeRepository outcomeRepository, final TransactionEntityContextFactory contextFactory,
        final TransactionEntityTransformer entityTransformer) {
        this.outcomeRepository = outcomeRepository;
        this.contextFactory = contextFactory;
        this.entityTransformer = entityTransformer;
    }

    @Override
    public List<Transaction> findAll(final LocalDate start, final LocalDate end, final Long userId) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findAll(convertToDate(start), convertToDate(end), userId)
            .forEach(entity -> transactionList.add(entityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Override
    public List<Transaction> findAll(final Long userId) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findAll(userId)
            .forEach(entity -> transactionList.add(entityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Override
    public Optional<Transaction> findById(final Long id, final Long userId) {
        return outcomeRepository.findByIdAndUserId(id, userId).map(entityTransformer::transformToTransaction);
    }

    @Override
    public List<Transaction> findByTitle(final String title, final Long userId) {
        List<Transaction> transactionList = new ArrayList<>();
        outcomeRepository.findByTitleAndUserId(title, userId).forEach(entity ->
            transactionList.add(entityTransformer.transformToTransaction(entity)));
        return transactionList;
    }

    @Override
    @Transactional
    public Transaction save(final Transaction transaction, final Long userId) {
        OutcomeEntity entityToSave = createOutcomeEntity(transaction, userId);
        OutcomeEntity response = outcomeRepository.save(entityToSave);
        return entityTransformer.transformToTransaction(response);
    }

    @Override
    public Transaction update(final Transaction transaction, final Long userId) {
        return save(transaction, userId);
    }

    @Override
    @Transactional
    public Transaction delete(final Transaction transaction, final Long userId) {
        OutcomeEntity entityToDelete = createOutcomeEntity(transaction, userId);
        outcomeRepository.delete(entityToDelete);
        return transaction;
    }

    private Date convertToDate(final LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private OutcomeEntity createOutcomeEntity(final Transaction transaction, final Long userId) {
        TransactionEntityContext transactionEntityContext = contextFactory.create(transaction, userId);
        return entityTransformer.transformToOutcomeEntity(transactionEntityContext);

    }

}
