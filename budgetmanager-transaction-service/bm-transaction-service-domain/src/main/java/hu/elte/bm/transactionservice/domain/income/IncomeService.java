package hu.elte.bm.transactionservice.domain.income;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public interface IncomeService {

    /**
     * Returns a list of Incomes between the two date (including the dates).
     * If there is no result it will returns with an empty list.
     *
     * @param start {@link LocalDate} the first possible date of the period. Cannot be null.
     * @param end   {@link LocalDate} the last possible date of the period. Cannot be null.
     * @return list of {@link Income}.
     * @throws IllegalArgumentException if start or end is null.
     */
    List<Income> getIncomeList(LocalDate start, LocalDate end) throws IllegalArgumentException;

    /**
     * Returns a list of Incomes from the date parameter (including).
     * If there is no result it will returns with an empty list.
     *
     * @param start {@link LocalDate} the first possible date of the period. Cannot be null.
     * @return list of {@link Income}.
     * @throws IllegalArgumentException if start is null.
     */
    List<Income> getIncomeList(LocalDate start) throws IllegalArgumentException;

    /**
     * Save the income into the repository.
     *
     * @param income {@link Income} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Income>} If it was saved the object contains the id as well.
     * If the income has saved already it returns an empty optional.
     * @throws IllegalArgumentException if income is null.
     */
    Optional<Income> saveIncome(Income income) throws IllegalArgumentException;

    /**
     * Update the existing income in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param income {@link Income} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Income>} If it was updated the object contains the modified values.
     * If the income has locked already it returns an empty optional.
     * @throws IllegalArgumentException if income is null.
     */
    Optional<Income> updateIncome(Income income) throws IllegalArgumentException;

    /**
     * Delete the existing income in the repository if the budget period has not been locked
     * (e.g. at the end of the month).
     *
     * @param income {@link Income} is extends {@link Transaction}. Cannot be null.
     * @return It returns a new {@link Optional<Income>} If it was updated the object contains the modified values.
     * If the income has locked already it returns an empty optional.
     * @throws IllegalArgumentException if income is null.
     */
    Optional<Income> deleteIncome(Income income) throws IllegalArgumentException;

}
