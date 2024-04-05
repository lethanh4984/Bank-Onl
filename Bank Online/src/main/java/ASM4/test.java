package ASM4;

import ASM4.dao.AccountDao;
import ASM4.model.Account;
import ASM4.model.SavingAccount;
import ASM4.model.TransactionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<Account> saveAccounts = new ArrayList<>();
        List<Account> accounts = AccountDao.list();
        for (Account account : AccountDao.list()) {
            if(account.getCustomerId().equals("022333111222")){
                saveAccounts.add(account);
            }
        }
        for (Account account : saveAccounts) {
            System.out.printf("%-14s%8s      |  %-28s  |%,.2fđ\n", account.getCustomerId(), account.getAccountNumber(), account instanceof SavingAccount ? "Saving" : "", account.getBalance());
        }



    }
}

 class Transaction implements Serializable {
    private List<ASM4.model.Transaction> transactions = new ArrayList<>();
    public static final long serialVersionUID=1;
    private String id;
    private String accountNumber;
    private double amount;
    private String time;
    private boolean status;
    private TransactionType type;


    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactions=" + transactions +
                ", id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", time='" + time + '\'' +
                ", status=" + status +
                '}';
    }
}