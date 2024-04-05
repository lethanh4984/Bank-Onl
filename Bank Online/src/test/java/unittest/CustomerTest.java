package unittest;


import ASM4.dao.AccountDao;
import ASM4.model.Account;
import ASM4.model.Customer;
import ASM4.model.SavingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {
    Customer customer = new Customer("012333222111", "Le");
    SavingAccount account = new SavingAccount("012319", 10000000, "012333222111");

    SavingAccount newAccount = new SavingAccount("129123", 10000000, "012333222111");

    List<Account> accountList;

    @BeforeEach
    public void unit() {
        accountList = AccountDao.list();
        accountList.add(account);
        accountList.add(newAccount);

    }
    @Test
    void getAccount() {

        List<Account> exceptedAccounts = new ArrayList<>();
        exceptedAccounts.add(account);
        exceptedAccounts.add(newAccount);
        assertEquals(exceptedAccounts, customer.getAccounts());
    }

    @Test
    void isPremiumCustomer() {
        assertEquals(true, customer.isPremiumCustomer());
    }


    @Test
    void totalBalance() {
        assertEquals(20000000, customer.totalBalance());
    }



}