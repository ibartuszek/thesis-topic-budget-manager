package hu.elte.bm.transactionservice.domain.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    /**
     * Returns a list of Transactions between the two date (including the dates).
     * If there is no result it will returns with an empty list.
     *
     * @param start              {@link LocalDate} the first possible date of the period. Cannot be null.
     * @param end                {@link LocalDate} the last possible date of the period. If it is null, it must be today.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return list of {@link Transaction}.
     */
    List<Transaction> findAllTransaction(LocalDate start, LocalDate end, TransactionContext transactionContext);

    /**
     * Save the transaction into the repository.
     *
     * @param transaction        {@link Transaction} object to save.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return It returns a new {@link Optional<Transaction>} If save was successful the object contains the id.
     * If the transaction has saved already it returns an empty optional.
     */
    Optional<Transaction> save(Transaction transaction, TransactionContext transactionContext);

    /**
     * Update the existing transaction in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction        {@link Transaction} object to update.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return It returns a new {@link Optional<Transaction>} If update was successful the object contains the modified values.
     * If the transaction has locked already it returns an empty optional.
     */
    Optional<Transaction> update(Transaction transaction, TransactionContext transactionContext);

    /**
     * Delete the existing transaction in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param transaction        {@link Transaction} object to delete.
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return It returns a new {@link Optional<Transaction>} If delete was successful the object contains the modified values.
     * If the transaction has locked already it returns an empty optional.
     */
    Optional<Transaction> delete(Transaction transaction, TransactionContext transactionContext);

    /**
     * Find the last day of the previous budget period.
     * If it can be found in the database the service add an extra day and gives it back as a start of the new period.
     * If it cannot be found it gives back a predefined time according to the current date.
     *
     * @param transactionContext {@link TransactionContext} holder object of: transactionType and userId.
     * @return the first available date: {@link LocalDate}
     */
    LocalDate getTheFirstDateOfTheNewPeriod(TransactionContext transactionContext);

}
