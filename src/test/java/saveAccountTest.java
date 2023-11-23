import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mongodb.client.model.Filters;

import static org.junit.jupiter.api.Assertions.*;

public class saveAccountTest {

    private static saveAccount saveAccount;

    @BeforeAll
    public static void setUp() {
        saveAccount = new saveAccount();
    }

    @Test
    public void testGenerateNewAccount() {
        String userName = "testUsername";
        String password = "testPassword";

        boolean result = saveAccount.generateNewAccount(userName, password);

        assertTrue(result);
        assertTrue(saveAccount.accountExist(userName));
         
        saveAccount.accountsCollection.deleteOne(Filters.eq("_id", userName));
    }

    @Test
    public void testGenerateExistingAccount() {
        String userName = "existingUser";
        String password = "existingPassword";

        saveAccount.generateNewAccount(userName, password);
        boolean result = saveAccount.generateNewAccount(userName, "newPassword");
        assertFalse(result);
        saveAccount.accountsCollection.deleteOne(Filters.eq("_id", userName));
    }

    @Test
    public void testLoginAccount() {
        String userName = "testLoginUser";
        String password = "testLoginPassword";

        saveAccount.generateNewAccount(userName, password);

        int result = saveAccount.loginAccount(userName, password);

        assertEquals(1, result);
        saveAccount.accountsCollection.deleteOne(Filters.eq("_id", userName));
    }

    @Test
    public void testLoginNoAccount() {
        String userName = "nonExistingUser";
        String password = "nonExistingPassword";

        int result = saveAccount.loginAccount(userName, password);

        assertEquals(-1, result);
    }
}
