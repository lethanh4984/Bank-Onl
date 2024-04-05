package ASM4;

import ASM4.model.Account;
import ASM4.model.Customer;
import ASM4.model.DigitalBank;
import ASM4.dao.AccountDao;
import ASM4.dao.CustomerDao;
import ASM4.service.BinaryFileService;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Asm4 {

    private static final Scanner scan = new Scanner(System.in);
    private static DigitalBank activeBank = new DigitalBank();
    private static List<Customer> customerDatFile;


    //viet thread cho cac chuc nang

    public static void main(String[] args) throws IOException {
        String id = "FX29055";
        String version = "@V4.0.0";
        System.out.println("+-----------+-------------------+----------+");
        System.out.println("|NGAN HANG SO | " + id + version);
        showMenu();
        System.out.print("Chon chuc nang:");

        while (true) {
            int functionNum;
            try {
                functionNum = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Chọn chức năng 0 hoặc 1 hoặc 2 hoặc 3 hoặc 4 hoặc 5 hoặc 6");
                showMenu();
                scan.nextLine();
                // scan.next() được dùng để đọc và bỏ qua giá trị không phù hợp
                continue;
            }
            if (functionNum == 1) {
                showCustomerList();
                showMenu();
            } else if (functionNum == 2) {
                addCustomer();
                showMenu();
            } else if (functionNum == 3) {
                addSavingAccount();
                showMenu();
            } else if (functionNum == 4) {
                transferMoney();
                showMenu();
            } else if (functionNum == 5) {
                withdraw();
                showMenu();
            } else if (functionNum == 6) {
                showTransactions();
                showMenu();
            } else if (functionNum == 0) {
                break;
            }
        }
    }

    public static void showMenu() {
        System.out.println("+-----------+-------------------+-----------+");
        System.out.println("|  1. Xem danh sách khách hàng              |");
        System.out.println("|  2. Nhập danh sách khách hàng             |");
        System.out.println("|  3. Thêm tài khoản ATM                    |");
        System.out.println("|  4. Chuyển tiền                           |");
        System.out.println("|  5. Rút tiền                              |");
        System.out.println("|  6. Tra cứu lịch sử giao dịch             |");
        System.out.println("|  0. Thoát                                 |");
        System.out.println("+-----------+-------------------+------------+");
    }

    //Chức năng 1
    public static void showCustomerList() {
        // đọc file customer.dat ở binaryFileService
        customerDatFile = CustomerDao.customersList();

        if (customerDatFile.isEmpty()) {
            System.out.println("Danh sách chưa cập nhật. Vui lòng 'Nhập danh sách khách hàng'");
        } else {
            for (Customer customer : customerDatFile) {
                customer.displayInformation();
            }
        }
    }

    //Chức năng 2
    public static void addCustomer() throws IOException {
        System.out.println("+-----------+-------------------+-----------+");
        System.out.println("Nhập đường dẫn đến tệp: ");
        while (true) {
            String fileName = scan.next();
            Path fileNameToPath = FileSystems.getDefault().getPath(fileName);
            Path path = FileSystems.getDefault().getPath("store/customer.txt");
            //file Exit
            if (Files.exists(path)) {
                // xử lý file .txt ko tồn tại
                if (path.equals(fileNameToPath)) {
                    activeBank.addCustomer(path);
                    break;
                } else {
                    System.out.println("Đường dẫn chưa chính xác. Vui lòng nhập lại đường dẫn");
                }
            } else {
                System.out.println("File " + path + " không tồn tại. Vui lòng kiểm tra lại ");
            }

        }

    }


    //chức năng 3:
    public static void addSavingAccount() throws IOException {
        System.out.println("Nhập mã số khách hàng: ");
        customerDatFile = CustomerDao.customersList();
        while (true) {
            String customerId = scan.next();
            //lây customerId của Customer trong list .dat
            if (activeBank.isCustomerExisted(customerDatFile, customerId)) {
                activeBank.addSavingAccount(scan, customerId);
                break;
            } else if (customerId.toLowerCase().equals("no")) {
                break;
            } else {
                System.out.println("Khách hàng chưa có trong danh sách. Vui lòng nhập lại mã số khách hàng hoặc 'no' để thoát");
            }
        }
    }

    //Chức năng 4
    public static void transferMoney() throws IOException {
        customerDatFile = CustomerDao.customersList();
        System.out.println("Nhập mã số của khách hàng:");
        while (true) {
            String customerId = scan.next();
            if (activeBank.isCustomerExisted(customerDatFile, customerId)) {
                Customer sendCustomer = activeBank.getCustomerById(customerDatFile, customerId);
                if (sendCustomer.getAccounts().isEmpty()) {
                    //Kiểm tra customer có tài khoản ATM chưa
                    System.out.println("Khách hàng chưa có tài khoản. Vui lòng nhập mã số khách hàng khác");
                } else {
                    activeBank.transfer(scan, customerId);
                    break;
                }
            } else if (customerId.toLowerCase().equals("no")) {
                break;
            }
//            else if (activeBank.isCustomerExisted(customerDatFile, customerId)) {}
            else {
                System.out.println("Khách hàng chưa tồn tại trong danh sách. Vui lòng nhập lại hoặc 'no' để thoát.");
            }
        }
    }

    //Chức năng 5
    public static void withdraw() throws IOException {
        customerDatFile = CustomerDao.customersList();
        System.out.println("Nhập mã số của khách hàng:");
        String customerId;
        while (true) {
            customerId = scan.next();
            if (activeBank.isCustomerExisted(CustomerDao.customersList(), customerId)) {
                Customer customer = activeBank.getCustomerById(customerDatFile, customerId);
                if (!customer.getAccounts().isEmpty()) {
                    customer.displayInformation();
                    //gọi hàm withdraw
                    activeBank.withdraw(scan, customerId);
                } else {
                    System.out.println("Khách hàng chưa tạo tài khoản. Vui lòng thêm tài khoản cho khách hàng.");
                }
                break;
            } else {
                System.out.println("Khách hàng nhập chưa đúng vui lòng nhập lại");
            }
        }

    }

    //chức năng 6
    public static void showTransactions() {
        customerDatFile = CustomerDao.customersList();

        System.out.println("Nhập mã số khách hàng");
        String customerId;
        while (true) {
            customerId = scan.next();
            if (!activeBank.isCustomerExisted(customerDatFile, customerId)) {
                System.out.println("Khách hàng nhập chưa đúng. Vui lòng nhập lại");
            } else {
                break;
            }
        }
        Customer customer = activeBank.getCustomerById(customerDatFile, customerId);
        customer.displayInformation();
        System.out.println("+----------+---------------------------------------+----------+");
        for (Account account : customer.getAccounts()) {
            account.displaytransactionsList();
        }
        System.out.println("+----------+---------------------------------------+----------+");
    }


}


//Lưu ý : đọc file dùng FileInputStream và readUTF và readInt;...
// xem lại phần IO Exception
//common dùng để chứa các phần dùng chung ở chả chương trình