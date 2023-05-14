package expense.model;

import com.google.inject.persist.Transactional;
import jpa.GenericJpaDao;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.DoubleStream;

public interface ExpenseDao {
    @Transactional
    public List<Expense> findBetweenDates(LocalDate startDate, LocalDate endDate);

    public Double getBalance();

    public Double getSumOfExpenses();


    public Double getExpenseByCategory(Expense.MainCategory category);
    @Transactional
    public List<Expense> getSearch(String searchTerm, LocalDate startDate, LocalDate endDate, List<Expense.MainCategory> categories);

}
