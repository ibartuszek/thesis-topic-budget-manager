package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public interface IncomeDao {

    List<Transaction> findAll(LocalDate start, LocalDate end, Long userId);

    List<Transaction> findAll(LocalDate end, Long userId);

    List<Transaction> findAll(Long userId);

    Optional<Transaction> findById(Long id, Long userId);

    List<Transaction> findByTitle(String title, Long userId);

    Transaction save(Transaction transaction, Long userId);

    Transaction update(Transaction transaction, Long userId);

    void update(List<Transaction> transaction, Long userId);

    Transaction delete(Transaction transaction, Long userId);

}
