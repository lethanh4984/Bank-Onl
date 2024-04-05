package ASM4.model;


import java.io.Serializable;
import java.util.List;


public class Transaction implements Serializable {
    private List<Transaction> transactions;
    public static final long serialVersionUID= 1;
    private String id;
    private String accountNumber;
    private double amount;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    private String time;
    private boolean status;
    private TransactionType type;

    public Transaction( String accountNumber, double amount, String time, boolean status, TransactionType type) {
//        this.transactions = transactions;
//        this.id = String.valueOf(serialVersionUID);
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.time = time;
        this.status = status;
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "[GD]"+accountNumber+type+amount+time;
    }
}
