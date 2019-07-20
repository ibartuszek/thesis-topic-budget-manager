package hu.elte.bm.transactionservice.domain.income;

import java.util.List;

public interface IncomeService {

    List<Income> getIncomeList();

    Income saveIncome(Income income);

    Income updateIncome(Income income);

    Income deleteIncome(Income income);

}
