package unittest;


import ASM4.model.SavingAccount;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    SavingAccount account = new SavingAccount("012311", 10000000, "012333222113");
    SavingAccount newAccount = new SavingAccount("123123", 10000000, "012333222114");


    @Test
    void transfers() throws IOException {
        assertEquals(5000000, account.transfers(newAccount, 5000000));
    }

    @Test
    void withdraw() {
        assertEquals(2000000, account.withdraw(8000000));
    }
}