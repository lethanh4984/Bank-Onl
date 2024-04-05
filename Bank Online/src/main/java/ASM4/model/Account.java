package ASM4.model;

import ASM4.dao.AccountDao;
import ASM4.dao.TransactionDao;
import ASM4.exception.CustomerIdNotValidException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Account implements Serializable, ITransfer {
    public boolean accountPremium;
    private String accountNumber;
    private double balance;
    private String CustomerId;

    private List<Transaction> transactions;
    private Account receiveAccount;
    private double amount;

    public Account(String accountNumber, double balance, String customerId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        CustomerId = customerId;
    }

    public List<Transaction> getTransactions() {
//        transactions = TransactionDao.transactionList();
//        List<Transaction> newTransactions = new ArrayList<>();
//        for(Transaction transaction: transactions){
//            if(transaction.getAccountNumber().equals(getAccountNumber())){
//                newTransactions.add(transaction);
//            }
//        }
//        return  newTransactions;

        return TransactionDao.transactionList().stream().filter(transaction ->
                transaction.getAccountNumber().equals(getAccountNumber())).toList();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setCustomerId(String customerId) throws CustomerIdNotValidException {
        CustomerId = customerId;
    }

    public boolean isAccountPremium() {
        return accountPremium;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isPremiumAccount() {
        if (balance < 10000000) {
            return false;
        }
        return true;
    }

    public String toString() {
        return accountNumber + "|   " + balance;
    }


    public void displaytransactionsList() {

        for (Transaction transaction : this.getTransactions()) {

            System.out.printf("%2s%13s|%8s|%,22.2fđ|   %13s\n", "[GD]", getAccountNumber(), transaction.getType(), transaction.getAmount(), transaction.getTime());
        }
//        System.out.println("+----------+---------------------------------------+----------+");

    }

    public Transaction createTransaction(String accNum, double amount, String time, boolean status, TransactionType type) {
        Transaction transaction = new Transaction(getAccountNumber(), amount, time, status, type);

        return transaction;
    }

    @Override
    public double transfers(Account receiveAccount, double amount) throws IOException {
        return getBalance();
    }

}

