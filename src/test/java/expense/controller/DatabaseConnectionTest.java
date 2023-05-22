package expense.controller;

import com.google.inject.Injector;
import expense.model.ExpenseDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseConnectionTest {

    private DatabaseConnection databaseConnection;

    @BeforeEach
    public void setUp() {
        databaseConnection = new DatabaseConnection();
    }

    @Test
    public void testGetInstance() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        // Assert
        Assertions.assertNotNull(instance1);
        Assertions.assertSame(instance1, instance2);
    }

    @Test
    public void testGetInjector() {
        // Act
        Injector injector = databaseConnection.getInjector();

        // Assert
        Assertions.assertNotNull(injector);
    }

    @Test
    public void testInitDB() {
        //Todo
    }
}