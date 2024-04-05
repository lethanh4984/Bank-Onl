package unittest;

import ASM4.model.Account;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class AccountTest {

    @Test
    void isPremiumAccount() {
        Account account = new Account("012311",10000000,"022333111222");
        assertEquals(true,account.isPremiumAccount());
    }
}