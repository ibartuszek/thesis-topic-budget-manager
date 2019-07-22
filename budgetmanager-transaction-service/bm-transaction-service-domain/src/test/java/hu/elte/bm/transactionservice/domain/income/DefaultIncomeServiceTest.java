package hu.elte.bm.transactionservice.domain.income;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.elte.bm.transactionservice.domain.Currency;
import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

public class DefaultIncomeServiceTest {

    private static final LocalDate START = LocalDate.of(2019, 5, 1);
    private static final LocalDate END = LocalDate.of(2019, 6, 1);
    private static final long ID = 1L;
    private static final long NEW_ID = 2L;
    private static final String TITLE = "Title1";
    private static final String NEW_TITLE = "New Title";
    private static final double AMOUNT = 100.0d;
    private static final String CATEGORY_NAME = "Category Name";
    private static final boolean LOCKED = true;
    private static final boolean UNLOCKED = false;

    private DatabaseProxy databaseProxy;
    private IMocksControl control;

    private DefaultIncomeService underTest;

    @BeforeMethod
    public void setup() {
        control = EasyMock.createStrictControl();
        databaseProxy = control.createMock(DatabaseProxy.class);
        underTest = new DefaultIncomeService(databaseProxy);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetIncomeListWhenFirstParameterIsNull() {
        // GIVEN
        // WHEN
        underTest.getIncomeList(null, END);
        // THEN
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetIncomeListWhenSecondParameterIsNull() {
        // GIVEN
        // WHEN
        underTest.getIncomeList(START, null);
        // THEN
    }

    @Test
    public void testGetIncomeListWhenThereAreMoreIncomes() {
        // GIVEN
        List<Income> incomeList = createExampleIncomeList();
        EasyMock.expect(databaseProxy.getIncomeList(START, END)).andReturn(incomeList);
        control.replay();
        // WHEN
        List<Income> result = underTest.getIncomeList(START, END);
        // THEN
        control.verify();
        Assert.assertEquals(incomeList, result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSaveIncomeWhenParameterIsNull() {
        // GIVEN
        // WHEN
        underTest.saveIncome(null);
        // THEN
    }

    @Test
    public void testSaveIncomeWhenIncomeHasBeenSavedAlready() {
        // GIVEN
        Income income = createIncome(NEW_ID, NEW_TITLE, LocalDate.now(), UNLOCKED);
        List<Income> incomeList = createExampleIncomeList();
        incomeList.add(createIncome(NEW_ID, NEW_TITLE, LocalDate.now(), UNLOCKED));
        EasyMock.expect(databaseProxy.getIncomeList(LocalDate.now(), LocalDate.now())).andReturn(incomeList);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.saveIncome(income);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testSaveIncomeWhenIncomeCanBeSaved() {
        // GIVEN
        Income income = createIncome(NEW_ID, NEW_TITLE, LocalDate.now(), UNLOCKED);
        List<Income> incomeList = createExampleIncomeList();
        EasyMock.expect(databaseProxy.getIncomeList(LocalDate.now(), LocalDate.now())).andReturn(incomeList);
        EasyMock.expect(databaseProxy.saveIncome(income)).andReturn(income);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.saveIncome(income);
        // THEN
        control.verify();
        Assert.assertEquals(income, result.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateIncomeWhenParameterIsNull() {
        // GIVEN
        // WHEN
        underTest.updateIncome(null);
        // THEN
    }

    @Test
    public void testUpdateIncomeWhenIncomeCannotBeFoundInRepository() {
        // GIVEN
        Income updatedIncome = createIncome(NEW_ID, NEW_TITLE, LocalDate.now(), UNLOCKED);
        EasyMock.expect(databaseProxy.getIncomeById(NEW_ID)).andReturn(null);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.updateIncome(updatedIncome);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateIncomeWhenIncomeHasLocked() {
        // GIVEN
        Income updatedIncome = createIncome(ID, NEW_TITLE, LocalDate.now(), LOCKED);
        EasyMock.expect(databaseProxy.getIncomeById(ID)).andReturn(updatedIncome);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.updateIncome(updatedIncome);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testUpdateIncomeWhenIncomeCanBeUpdated() {
        // GIVEN
        Income income = createIncome(ID, TITLE, LocalDate.now(), UNLOCKED);
        Income updatedIncome = createIncome(ID, NEW_TITLE, LocalDate.now(), UNLOCKED);
        Income copyUpdatedIncome = createIncome(ID, NEW_TITLE, LocalDate.now(), UNLOCKED);
        EasyMock.expect(databaseProxy.getIncomeById(ID)).andReturn(income);
        EasyMock.expect(databaseProxy.updateIncome(updatedIncome)).andReturn(copyUpdatedIncome);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.updateIncome(updatedIncome);
        // THEN
        control.verify();
        Assert.assertEquals(updatedIncome, result.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeleteIncomeWhenParameterIsNull() {
        // GIVEN
        // WHEN
        underTest.deleteIncome(null);
        // THEN
    }

    @Test
    public void testDeleteIncomeWhenIncomeCannotBeFoundInRepository() {
        // GIVEN
        Income income = createIncome(ID, TITLE, LocalDate.now(), UNLOCKED);
        EasyMock.expect(databaseProxy.getIncomeById(ID)).andReturn(null);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.deleteIncome(income);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testDeleteIncomeWhenIncomeHasLocked() {
        // GIVEN
        Income income = createIncome(ID, TITLE, START, LOCKED);
        EasyMock.expect(databaseProxy.getIncomeById(ID)).andReturn(income);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.deleteIncome(income);
        // THEN
        control.verify();
        Assert.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testDeleteIncomeWhenIncomeCanBeDeleted() {
        // GIVEN
        Income income = createIncome(ID, TITLE, START, UNLOCKED);
        Income copyUpdatedIncome = Income.builder(income).build();
        EasyMock.expect(databaseProxy.getIncomeById(ID)).andReturn(income);
        EasyMock.expect(databaseProxy.updateIncome(income)).andReturn(copyUpdatedIncome);
        control.replay();
        // WHEN
        Optional<Income> result = underTest.updateIncome(income);
        // THEN
        control.verify();
        Assert.assertEquals(income, result.get());
    }

    private List<Income> createExampleIncomeList() {
        List<Income> incomeList = new ArrayList<>();
        incomeList.add(createIncome(ID, TITLE, START, UNLOCKED));
        return incomeList;
    }

    private Income createIncome(final Long id, final String title, final LocalDate date, final boolean locked) {
        return Income.builder()
            .withId(id)
            .withTitle(title)
            .withAmount(AMOUNT)
            .withCurrency(Currency.EUR)
            .withDate(date)
            .withMainCategory(createMainCategory())
            .withLocked(locked)
            .build();
    }

    private MainCategory createMainCategory() {
        return MainCategory.builder()
            .withId(ID)
            .withName(CATEGORY_NAME)
            .withCategoryType(CategoryType.INCOME)
            .build();
    }

}
