package expense.model;

import junit.framework.TestCase;
import org.junit.Before;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDaoImplTest extends TestCase {

    ExpenseDaoImpl expenseDaoImpl = new ExpenseDaoImpl();
    @Before
    public void generateData(){
        for(int i = 0; i < 30; i++){
            Expense expense = Expense.builder()
                    .title("title" + i)
                    .cost(500.0 + i*10)
                    .date(LocalDate.of(2000+i, 8, 1))
                    .type(i%2==0?Expense.Type.EXPENSE: Expense.Type.INCOME)
                    .category(Expense.MainCategory.values()[i%8])
                    .build();

            expenseDaoImpl.persist(expense);
        }


    }
    public void testFindBetweenDates() {

    }

    public void testGetBalance() {
    assertEquals(150.0,expenseDaoImpl.getBalance());
    }

    public void testGetSumOfExpenses() {
    }

    public void testGetExpenseByCategory() {
    }

    public void testGetSearch() {
    }
}