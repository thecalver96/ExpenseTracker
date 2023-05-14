package expense.model;

import com.google.inject.persist.Transactional;
import jpa.GenericJpaDao;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.DoubleStream;

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
