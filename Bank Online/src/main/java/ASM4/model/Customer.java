package ASM4.model;

import ASM4.dao.AccountDao;
import ASM4.dao.CustomerDao;
import ASM4.dao.TransactionDao;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Customer extends User implements Serializable {
    private List<Account> accounts;

    public Customer(String customerId, String name) {
        super(customerId, name);
    }

    public Customer(List<String> values) {
        this(values.get(0), values.get(1));
    }


    // tạo hàm isPremiumAccount()
    public boolean isPremiumCustomer() {
        for (Account account : getAccounts()) {
            if (account.isPremiumAccount()) {
                return true;
            }
        }
        return false;
    }

    // hàm thêm Account
    public void addAccount(Account newAccount) {
        // kiểm tra Account tồn tại hay ko cần viết
        // trong lớp bank
        accounts = AccountDao.list();
        for (Account account : getAccounts()) {
            if (!this.getCustomerId().equals(newAccount.getCustomerId())) {
                accounts.add(newAccount);
            }
        }
    }

    // hàm boolean kiểm tra Account tồn tại hay chưa
    public boolean isAccountExist(String accountNumber) {
        accounts = AccountDao.list();
        for (Account account : accounts) {
            if (accountNumber.equals(account.getAccountNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSendOrWithdrawAccountExist(String accountNumber) {
        for (Account account : getAccounts()) {
            if (accountNumber.equals(account.getAccountNumber())) {
                return true;
            }
        }
        return false;
    }

    // hàm tính tổng số dư
    public double totalBalance() {
        double totalBalance = 0;
        for (Account account : getAccounts()) {
            totalBalance += account.getBalance();
        }
        return totalBalance;
    }

    // Viết thêm phương thức duyệt các account của 1 Customer.
    // Nếu có 1 cái Premium thì Customer đó là Premium
    public void displayInformation() {
        // String format: - thêm khoảng trăng tay phải và ko có dâu thêm bên tay trái :nếu lớn hơn 10,15,...
        //s là string, d là số , đ đơn vị kèm theo
        System.out.printf("%-10s     |%15s      |%8s  |%,.2fđ\n", getCustomerId(), getName(), (isPremiumCustomer() ? "Premium" : "Normal"), totalBalance());
        //String format

        int i = 1;
        for (Account account : getAccounts()) {
            System.out.printf("%-3d%8s      |  %-28s  |%,.2fđ\n", (i++), account.getAccountNumber(), account instanceof SavingAccount ? "Saving" : "", account.getBalance());
        }
    }


    public static boolean checkAccountNum(String accountNum) {
        boolean isCustomerNum;
        if (accountNum.length() != 6)
        // Kiểm tra account number có đúng 6 kí tự ko
        {
            return false;
        } else {
            try {
                // bắt lỗi nhập account number. Nếu đúng tiếp tục thực thi khối try
                Long.parseLong(accountNum);
                return true;
            } catch (NumberFormatException e) {
                // thực thi khi nhập account number sai
                return false;
            }
        }
    }


    public List<Account> getAccounts() {

//        List<Account> saveAccounts = new ArrayList<>();
//        for (Account account : AccountDao.list()) {
//            if (account.getCustomerId().equals(super.getCustomerId())) {
//                saveAccounts.add(account);
//            }
//        }
//        return saveAccounts;


//        return AccountDao.list().stream().filter(item -> item.getCustomerId().equals(this.getCustomerId())).collect(Collectors.toList());
        return AccountDao.list().stream().filter(account ->
                account.getCustomerId().equals(super.getCustomerId())).toList();
//            return account;


//        return accountList;
    }


    public Account getAccountByAccountNum(String accountNum) {
        accounts = AccountDao.list();
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNum)) {
                return account;
            }
        }
        return null;
    }

    public void showCustomerName(List<Customer> customers, String accountNum) {
        for (Customer customer : customers) {
            for (int i = 0; i < customer.getAccounts().size(); i++) {
                if (customer.getAccounts().get(i).getAccountNumber().equals(accountNum)) {
                    System.out.println("Gửi tiền tới tài khoản: " + accountNum + " | " + customer.getName());
                    break;
                }
            }
        }
    }

    public void transfer(Scanner scan) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sendAccountNum;
        System.out.println("Nhập số tài khoản: ");
        while (true) {
            sendAccountNum = scan.next();
            if (!isSendOrWithdrawAccountExist(sendAccountNum)) {
                System.out.println("Tài khoản chưa chính xác. Vui lòng nhập lại");
            } else {
                break;
            }
        }

        // nhập số tài khoản nhận
        String receiveAccountNum;
        System.out.println("Nhập số tài khoản nhận (hoặc 'No' để thoát): ");
        while (true) {
            receiveAccountNum = scan.next();
//            DigitalBank.get
            if (!isAccountExist(receiveAccountNum)) {
                System.out.println("Tài khoản nhận chưa chính xác. Vui lòng nhập lại");
            } else if (sendAccountNum.equals(receiveAccountNum)) {
                System.out.println("Tài khoản chuyển không đươc trùng tài khoản nhận");
            } else if (receiveAccountNum.toLowerCase().equals("no")) {
                break;
            } else {
                break;
            }
        }

        SavingAccount receiveAccount = (SavingAccount) getAccountByAccountNum(receiveAccountNum);
        SavingAccount sendAccount = (SavingAccount) getAccountByAccountNum(sendAccountNum);

        showCustomerName(CustomerDao.customersList(), receiveAccountNum);

        //nhâp số tiền cần chuyển
        System.out.println("Nhập số tiền chuyển: ");
        double amount = 0;
        double receiveAmount = 0;
        double sendAmount = 0;
        String confirmChar = "n";
        while (true) {
            try {
                amount = scan.nextDouble();
                if (amount == 0) {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Số dư không hợp lệ. Vui lòng nhập lại");
                scan.next();
                continue;
            }

            if (sendAccount.getBalance() - amount >= 50000) {
                System.out.printf("Xác nhận  thực hiện chuyển " + String.format("%,.2fđ", amount) + " từ tài khoản [" + sendAccountNum
                        + "] tới tài khoản [" + receiveAccountNum + "]  (Y/N):");
                while (true) {
                    confirmChar = scan.next();
                    if (confirmChar.toLowerCase().equals("y")) {
                        System.out.println("Chuyển tiền thành công, biên lai giao dịch:");
                        break;
                    } else if (confirmChar.toLowerCase().equals("n")) {
                        System.out.println("Giao dịch không thực hiện.");
                        break;
                    } else {
                        System.out.println("Vui lòng nhập lại 'y' hoặc 'n'.");
                    }
                }
                break;
            } else {
                System.out.println("Số dư trong tài khoản không đủ 50,000vnd.Vui lòng nhập lại số tiền.\nHoặc '0' để thoát");
            }
        }


        if (confirmChar.toLowerCase().equals("y") && amount != 0) {
            List<Transaction> transactions = TransactionDao.transactionList();
            sendAccount.transfers(receiveAccount, amount);
            sendAccount.log(amount, TransactionType.TRANSFER, receiveAccountNum);

            receiveAmount = amount;
            sendAmount = -amount;

            // tạo transaction cho sendAccount và receiveAccount
            Transaction sendTransaction = new Transaction(sendAccountNum, sendAmount, time, true, TransactionType.TRANSFER);
            Transaction receiveTransaction = new Transaction(receiveAccountNum, receiveAmount, time, true, TransactionType.TRANSFER);

            // thêm transaction vào transaction của từng account
            transactions.add(sendTransaction);
            transactions.add(receiveTransaction);

            //Save transactionList
            TransactionDao.save(transactions);
        }
    }

    public void withdraw(Scanner scan) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        accounts = AccountDao.list();
        // nhập số tài khoản rút
        System.out.println("Nhập số tài khoản:");
        String accNum;
        while (true) {
            accNum = scan.next();
            if (accNum.toLowerCase().equals("no")) {
                break;
            } else if (!isSendOrWithdrawAccountExist(accNum)) {
                System.out.println("Tài khoản nhập chưa chính xác. Vui lòng nhập lại.\n Hoặc 'No' để thoát");
            } else {
                break;
            }
        }

//        showCustomerName(CustomerDao.customersList(), accNum);
        SavingAccount withdrawAccount = (SavingAccount) getAccountByAccountNum(accNum);
        if (!accNum.equals("no")) {
            double amount;
            System.out.println("Nhập số tiền rút:");
            while (true) {
                try {
                    amount = scan.nextDouble();
                    if (withdrawAccount.isAccepted(amount)) {
                        System.out.println("Rút tiền thành công biên lai giao dịch:");
                        break;
                    } else if (withdrawAccount.getBalance() - amount < 50000) {
                        System.out.println("Số dư không đủ 50,000vnd để thực hiện rút tiền. Vui lòng nhập lại hoặc '0' để thoát");
                    } else if (!withdrawAccount.isPremiumAccount() && amount > 5000000) {
                        System.out.println("Số tiền tối đa được rút là 5,000,000vnđ.Vui lòng nhập lại hoặc '0' để thoát");
                    } else if (amount == 0) {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Số tiền nhâp chưa chính xác vui lòng kiểm tra lại");
                    scan.next();
                }

            }

            if (withdrawAccount.isAccepted(amount)) {

                withdrawAccount.withdraw(amount);
                AccountDao.update(withdrawAccount);
                AccountDao.save(AccountDao.list());
                withdrawAccount.log(amount, TransactionType.WITHDRAW, withdrawAccount.getAccountNumber());

                //tạo số tiền rút là số âm để truyền vào transaction
                double withdrawAmount = -amount;

                //đọc list transaction
                List<Transaction> transactions = TransactionDao.transactionList();

                //tao transaction cho withdraw
                Transaction transaction = withdrawAccount.createTransaction(accNum, withdrawAmount, time, withdrawAccount.isAccepted(amount), TransactionType.WITHDRAW);
                transactions.add(transaction);

                // lưu list transaction
                TransactionDao.save(transactions);
            }
        }

    }
}
// có thể tao 1 class để định dạng
//các kiểu và để static

