package hu.elte.bm.transactionservice.domain.income;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;

@Service("incomeService")
public class DefaultIncomeService implements IncomeService {

    @Autowired
    private DatabaseFacade databaseFacade;

    @Override
    public List<Income> getIncomeList() {
        return this.databaseFacade.findAllIncome();
    }

    @Override
    public Income saveIncome(Income income) {
        return databaseFacade.saveIncome(income);
    }

    @Override
    public Income updateIncome(Income income) {
        return databaseFacade.updateIncome(income);
    }

    @Override
    public Income deleteIncome(Income income) {
        return databaseFacade.deleteIncome(income);
    }
}
