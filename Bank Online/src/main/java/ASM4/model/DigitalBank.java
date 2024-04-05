package ASM4.model;

import ASM4.dao.AccountDao;
import ASM4.dao.CustomerDao;
import ASM4.dao.TransactionDao;
import ASM4.exception.CustomerIdNotValidException;
import ASM4.file.TextFileService;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class DigitalBank extends Bank {
    private List<Customer> customerDatFile;
    private final List<Account> accountList = AccountDao.list();
//    private List<Transaction> transactions;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    String time = dateFormat.format(date);

//    public List<Customer> getCustomerDatFile() {
//        return customerDatFile;
//    }


//    public void setCustomerDatFile(List<Customer> customerDatFile) {
//        this.customerDatFile = customerDatFile;
//    }

    //tìm khách hàng theo customerId
    public Customer getCustomerById(List<Customer> customers, String customerId) {
        //method tìm Customer trùng với customerId
//        customerDatFile = CustomerDao.customersList();
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    //
    public void addCustomer(Path fileName) throws IOException {
        List<List<String>> customerListOfTextFile = TextFileService.readFile(fileName);
        customerDatFile = CustomerDao.customersList();

        //customerDatFile = null

        //nên
        // từ list list String chuyển thành list customer (file txt)
        // lấy list id khách hàng từ list customer (file dat)
        // so sánh các điều kiện và làm tiếp

//        List<Customer> customersList = customerListOfTextFile.stream()
//                // tạo ra 1 list customer từ file text
//                .map(value -> {
//                    String newCustomerId = value.get(0);
//                    String name = value.get(1);
//                    return new Customer(newCustomerId, name);
//                })
//                .toList();
//
//        List<String> customerIds = customerListOfTextFile.stream().map(value -> {
//            String customerId = value.get(0);
//            return customerId;
//        }).toList();
//
//        if (!customerListOfTextFile.isEmpty()) {
//            for (int i = 0; i < customerIds.size(); i++) {
//                if (isCustomerExisted(customerDatFile, customerIds.get(i))){
//                    System.out.println("Khách hàng " + customerIds.get(i) + " đã tồn tại");
//                }else {
//                    customerDatFile.add(customersList.get(i));
//                }
//
//            }
//        }else {
//            System.out.println("Danh sách " + fileName + " chưa có dữ liệu. Vui lòng cập nhật dữ liệu");
//        }


        if (!customerListOfTextFile.isEmpty()) {
            for (int j = 0; j < customerListOfTextFile.size(); j++) {
                String id = customerListOfTextFile.get(j).getFirst();
                //duyệt qua list của file txt và lấy customerId

                if (isCustomerExisted(customerDatFile, id)) {
                    //duyệt vòng lặp để kiểm tra khách hàng tồn tại
                    System.out.println("Khách hàng " + id + " đã tồn tại");
                } else {
                    if (isCustomerId(id)) {
                        List<Customer> customers = customerListOfTextFile.stream()
                                // tạo ra 1 list customer từ file text
                                .map(value -> {
                                    String newCustomerId = value.get(0);
                                    String name = value.get(1);
                                    return new Customer(newCustomerId, name);
                                }).toList();

                        if (isCustomerExisted(customers, id)) {
                            customerDatFile.add(getCustomerById(customers, id));
                            System.out.println("Đã thêm khách hàng " + id + " vào danh sách khách hàng");
                        }
                        //thêm Customer vào list customer.dat
                    } else {
                        throw new CustomerIdNotValidException("Thông tin khách hàng " + id + " chưa chính xác. Vui lòng kiểm tra lại.");
                    }
                }
            }
            CustomerDao.save(customerDatFile);
        } else {
//            throw new IOException("Danh sách " + fileName + " chưa có dữ liệu. Vui lòng cập nhật dữ liệu");
            System.out.println("Danh sách " + fileName + " chưa có dữ liệu. Vui lòng cập nhật dữ liệu");
        }
    }


    public void addSavingAccount(Scanner scan, String customerId) throws IOException {
        List<Transaction> transactionList = TransactionDao.transactionList();

        //nhập số tài khoản tạo
        String accountNum;
        System.out.println("Nhập số tài khoản gồm 6 chữ số: ");
        while (true) {
            accountNum = scan.next();
            if (!Customer.checkAccountNum(accountNum)) {
                System.out.println("Số tài khoản nhập chưa đúng. Vui lòng nhập lại");
            } else if (isAccountExisted(accountList, accountNum)) {
                System.out.println("Số tài khoản đã được sử dụng. Vui lòng nhập lại");
            } else {
                break;
            }
        }

        // nhập số dư cho tài khoản
        System.out.println("Nhập số dư tài khoản >= 50,000: ");
        double balance = 0;
        while (true) {
            try {
                balance = scan.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Số dư không hợp lệ. Vui lòng nhập lại");
                scan.next();
                continue;
            }

            if (balance >= 50000 && balance % 10000 == 0) {
                SavingAccount account = new SavingAccount(accountNum, balance, customerId);

                accountList.add(account);

                //tao transaction cho kiểu Deposit
                Transaction transaction = new Transaction(accountNum, balance, time, true, TransactionType.DEPOSIT);

                transactionList.add(transaction);

                // lưu vào file
                AccountDao.save(accountList);
                TransactionDao.save(transactionList);
                System.out.println("Tạo tài khoản thành công");
                break;
            } else {
                System.out.println("Số dư không nhỏ hơn 50,000vnd và chia hết cho 10000vnd");
            }
        }
    }


    //phương thức để rút tiền
    public void withdraw(Scanner scan, String customerId) throws IOException {
        customerDatFile = CustomerDao.customersList();
        Customer customer = getCustomerById(customerDatFile, customerId);
        customer.withdraw(scan);
    }


    public boolean isAccountExisted(List<Account> accounts, String accountNum) {
//        for (Account account : accounts) {
//            if (account.getAccountNumber().equals(accountNum)) {
//                return true;
//            }
//        }

        return accounts.stream().anyMatch(account -> {
            return account.getAccountNumber().equals(accountNum);
//            return true;
        });
//        return false;
    }


    public boolean isCustomerExisted(List<Customer> customers, String customerId) {

        return customers.stream().anyMatch(customer -> {
            //stream kiểm tra có customer nào trùng customerId ko
            return customer.getCustomerId().equals(customerId);
            //trả về true nếu customer equals (contains là kiểm tra gần đúng nên sai)customerId ;
            // false nếu ko equals cusstomerId
        });
    }


    //chuyển tiền
    public void transfer(Scanner scan, String customerId) throws IOException {
        customerDatFile = CustomerDao.customersList();
        Customer sendCustomer = getCustomerById(customerDatFile, customerId);
        System.out.println("+-----------+-------------------+-----------+");
        sendCustomer.displayInformation();
        System.out.println("+-----------+-------------------+-----------+");
        sendCustomer.transfer(scan);


    }


    public Account getAccountByAccountNum(List<Account> accounts, String accountNum) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNum)) {
                return account;
            }
        }
        return null;
    }


    public boolean isCustomerId(String customerId) throws CustomerIdNotValidException {
        if (customerId.length() != 12)
        // Kiểm tra CCCD có đúng 12 kí tự ko
        {
            return false;
        } else {
            try {
                // bắt lỗi nhập CCCD. Nếu đúng tiếp tục thực thi khối try
                Long.parseLong(customerId);
                return true;
            } catch (NumberFormatException e) {
                // thực thi khi nhập CCCD sai
                return false;
            }
        }
    }




}
