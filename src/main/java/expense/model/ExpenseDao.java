package expense.model;

import com.google.inject.persist.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDao {
    @Transactional
    List<Expense> findBetweenDates(LocalDate startDate, LocalDate endDate);

    @Transactional
    Double getBalance();

    @Transactional
    Double getSumOfExpenses();

    @Transactional
    Double getExpenseByCategory(Expense.MainCategory category);

    @Transactional
    List<Expense> getSearch(String searchTerm, LocalDate startDate, LocalDate endDate, List<Expense.MainCategory> categories);

}
