package unittest;

import ASM4.dao.AccountDao;
import ASM4.dao.CustomerDao;
import ASM4.model.Account;
import ASM4.model.Customer;
import ASM4.model.DigitalBank;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DigitalBankTest {
    Customer customer = new Customer("012333222112","Thanh");
    Account account = new Account("124123",10000000,"012333222112");
    DigitalBank digitalBank = new DigitalBank();
    List<Account> accountList;
    List<Customer> customers;

    @BeforeEach
    public void unit(){
        customers = CustomerDao.customersList();
        accountList= AccountDao.list();
        customers.add(customer);
        accountList.add(account);
    }

    @Test
    void isAccountExisted() {
        assertEquals(true,digitalBank.isAccountExisted(accountList,"124123"));
    }

    @Test
    void isCustomerExisted() {
//        unit();
        assertEquals(true,digitalBank.isCustomerExisted(customers,"012333222112"));
    }

    @Test
    void getAccountByAccountNum() {
        assertEquals(account,digitalBank.getAccountByAccountNum(accountList,"124123"));
    }

    @Test
    void isCustomerId() {
        assertEquals(true, digitalBank.isCustomerId("012333222112"));
    }

}