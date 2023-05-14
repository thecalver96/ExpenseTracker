package expense.model;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDaoInt {
    List<Expense> findAll();
    List<Expense> getSearch(String searchQuery, LocalDate startDate, LocalDate endDate, List<Expense.MainCategory> categories);
    Double getBalance();
    Double getSumOfExpenses();
    Double getExpenseByCategory(Expense.MainCategory category);
}
