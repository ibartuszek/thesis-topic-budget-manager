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
     * Update the existing transaction in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction {@link Transaction} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Transaction>} If update was successful the object contains the modified values.
     * If the transaction has locked already it returns an empty optional.
     */
    Optional<Transaction> update(Transaction transaction);

    /**
     * Delete the existing transaction in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction {@link Transaction} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Transaction>} If delete was successful the object contains the modified values.
     * If the transaction has locked already it returns an empty optional.
     */
    Optional<Transaction> delete(Transaction transaction);

    /**
     * Find the last day of the previous budget period.
     * If it can be found in the database the service add an extra day and gives it back as a start of the new period.
     * If it cannot be found it gives back a predefined time according to the current date.
     *
     * @param type the type of the transaction: {@link TransactionType}
     * @return the first available date: {@link LocalDate}
     */
    LocalDate getTheFirstDateOfTheNewPeriod(TransactionType type);

    /**
     * Find the transaction by id and gives back its locked field.
     *
     * @param id   the id of the {@link Transaction}
     * @param type the type of the transaction: {@link TransactionType}
     * @return true if the transaction is locked already or if it cannot be found in the repository.
     */
    boolean isLockedTransaction(Long id, TransactionType type);
}
