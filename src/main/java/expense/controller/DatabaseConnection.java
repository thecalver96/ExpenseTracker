package expense.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import lombok.Getter;

//singleton
@Getter
public final class DatabaseConnection {

    private static DatabaseConnection INSTANCE;
    private final Injector injector;



    DatabaseConnection() {
        this.injector = Guice.createInjector(new PersistenceModule("ExpenseTracker"));
    }

    public static DatabaseConnection getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseConnection();
        }

        return INSTANCE;
    }


}
