package ASM4.model;

import ASM4.dao.AccountDao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavingAccount extends Account implements Withdraw, IReport {
    int SAVINGS_ACCOUNT_MAX_WITHDRAW = 5000000;
//    double restOfSavingAccount;

    public SavingAccount(String accountNumber, double balance, String customerId) {
        super(accountNumber, balance, customerId);
    }

    @Override
    public double withdraw(double amount) {
        //viet lai dieu kien isAccepted() vaf truyen vao if
        //sua lai ham withdraw
        if (isAccepted(amount)) {
            double newBalance = this.getBalance() - amount;
            setBalance(newBalance);
        }
        return getBalance();
    }

    //kiểm tra lại cách tính số dư


    @Override
    public double transfers(Account receiveAccount, double amount) throws IOException {
        //tính số dư mới của sendAccount và receiveAccount
        double newBalanceReceiveAcc = receiveAccount.getBalance() + amount;
        double newBalanceSendAcc = getBalance() - amount;

        //gán số dư mới cho sendAccount bằng hàm setBalance
        super.setBalance(newBalanceSendAcc);


        //gán số dư mới cho receiveAccount
        receiveAccount.setBalance(newBalanceReceiveAcc);

        //update sendAccount và receiveAccount
        AccountDao.update(receiveAccount);
        AccountDao.update(this);

        //lưu vào account list
        AccountDao.save(AccountDao.list());
//        log(amount, TransactionType.TRANSFER, receiveAccount.getAccountNumber());

        return getBalance();

    }

    @Override
    public boolean isAccepted(double amount) {
        if (amount >= 50000 && amount % 10000 == 0) {
            if (isPremiumAccount() && this.getBalance() - amount >= 50000) {
                return true;
            } else if (!isPremiumAccount() && amount < SAVINGS_ACCOUNT_MAX_WITHDRAW && getBalance() - amount >= 50000) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void log(double amount, TransactionType type, String receiveAccountNum) {
        System.out.println("+----------+------------------------------+----------+");
        System.out.println("   BIEN LAI GIAO DICH SAVINGS");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        Date year = new Date();
        System.out.printf("Ngay giao dich:%40s\n", dateFormat.format(date));
        System.out.printf("ATM ID: DIGITAL-BANK-ATM%16s\n", yearFormat.format(year));
        if (type.equals(TransactionType.TRANSFER)) {
            System.out.printf("So tai khoan:%29s\n", getAccountNumber());
            System.out.printf("So tai khoan nhan:%24s\n", receiveAccountNum);
            System.out.printf("So tien:%,41.2fđ\n", amount);
            System.out.printf("So du:%,44.2f\n", getBalance());
            System.out.printf("Phi + vat:%39sđ\n", "0");
            System.out.println("+----------+------------------------------+----------+");
        } else if (type.equals(TransactionType.WITHDRAW)) {
            System.out.printf("So tai khoan:%29s\n", getAccountNumber());
            System.out.printf("So tien:%,41.2fđ\n", amount);
            System.out.printf("So du:%,44.2f\n", getBalance());
            System.out.printf("Phi + vat:%39sđ\n", "0");
            System.out.println("+----------+------------------------------+----------+");
        }
    }


}
