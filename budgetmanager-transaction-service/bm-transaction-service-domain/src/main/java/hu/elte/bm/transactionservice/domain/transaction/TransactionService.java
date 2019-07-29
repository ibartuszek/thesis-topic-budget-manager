package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    /**
     * Returns a list of Transactions between the two date (including the dates).
     * If there is no result it will returns with an empty list.
     *
     * @param start           {@link LocalDate} the first possible date of the period. Cannot be null.
     * @param end             {@link LocalDate} the last possible date of the period. Cannot be null.
     * @param transactionType {@link TransactionType} the type of the transaction. Cannot be null.
     * @return list of {@link Transaction}.
     */
    List<Transaction> findAllTransaction(LocalDate start, LocalDate end, TransactionType transactionType);

    /**
     * Returns a list of Transactions from the date parameter (including).
     * If there is no result it will returns with an empty list.
     *
     * @param start           {@link LocalDate} the first possible date of the period. Cannot be null.
     * @param transactionType {@link TransactionType} the type of the transaction. Cannot be null.
     * @return list of {@link Transaction}.
     */
    List<Transaction> findAllTransaction(LocalDate start, TransactionType transactionType);

    /**
     * Save the transaction into the repository.
     *
     * @param transaction {@link Transaction}, cannot be null.
     * @return It returns a new {@link Optional<Transaction>} If save was successful the object contains the id.
     * If the transaction has saved already it returns an empty optional.
     */
    Optional<Transaction> save(Transaction transaction);

    /**
     * Update the existing income in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction {@link Transaction} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Transaction>} If update was successful the object contains the modified values.
     * If the income has locked already it returns an empty optional.
     */
    Optional<Transaction> update(Transaction transaction);

    /**
     * Delete the existing transaction in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction {@link Transaction} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Transaction>}.
     * If the transaction has locked already it returns an empty optional.
     */
    Optional<Transaction> delete(Transaction transaction);

}
