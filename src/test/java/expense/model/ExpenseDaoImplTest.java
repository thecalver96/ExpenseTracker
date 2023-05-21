package expense.model;


import com.google.inject.Injector;
import expense.controller.DatabaseConnection;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import java.util.List;

import static org.mockito.Mockito.*;
public class ExpenseDaoImplTest {

    @InjectMocks
    private ExpenseDaoImpl expenseDao;

    @Mock
    private DatabaseConnection databaseConnectionMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllExpenses() {
        // Arrange
        Expense expense1 = Expense.builder()
                .id(1)
                .title("first")
                .date(LocalDate.now())
                .type(Expense.Type.EXPENSE)
                .cost(123.1123)
                .category(Expense.MainCategory.DRESSING)
                .build();
        Expense expense2 = Expense.builder()
                .id(2)
                .title("second")
                .date(LocalDate.now().minusDays(12))
                .type(Expense.Type.INCOME)
                .cost(1999.1123)
                .category(Expense.MainCategory.ENTERTAINMENT)
                .build();
        List<Expense> expectedExpenses = List.of(expense1, expense2);

        // Act
        when(databaseConnectionMock.getInjector().getInstance(ExpenseDaoImpl.class)).thenReturn(expenseDao);
        when(expenseDao.findAll()).thenReturn(expectedExpenses);
        List<Expense> actualExpenses = expenseDao.findAll();

        // Assert
        Assertions.assertEquals(expectedExpenses.size(), actualExpenses.size());
        Assertions.assertEquals(expectedExpenses.get(0), actualExpenses.get(0));
        Assertions.assertEquals(expectedExpenses.get(1), actualExpenses.get(1));

        verify(databaseConnectionMock, times(1)).getInjector().getInstance(ExpenseDaoImpl.class);
        verify(expenseDao, times(1)).findAll();
        verifyNoMoreInteractions(databaseConnectionMock, expenseDao);
    }
}