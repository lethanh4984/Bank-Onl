package ASM4.dao;

import ASM4.model.Account;
import ASM4.service.BinaryFileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private final static String FILE_PATH = "store/accounts.dat";


    public static void save(List<Account> accounts) throws IOException {
        //lưu list vào file
        BinaryFileService.writeFile(FILE_PATH, accounts);
    }

    public static List<Account> list() {
        return BinaryFileService.readFile(FILE_PATH);
    }

    public static void update( Account editAccount) throws IOException {
        List<Account> accounts = list();
        //tải danh sach hien co
        boolean hasExist = accounts.stream().anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));
        //kiểm tra list bat ki phan tu nao cua list có accountNumber trùng với accountNumber cua editAccount hay không
        List<Account> updatedAccounts;
        //tạo list mới
        if (!hasExist) {
            //accountNum ko ton tai
            updatedAccounts = new ArrayList<>(accounts);//accounts là đối số của array list
            //tao arrayList có chứa các phần tử từ danh sách accounts ban đầu
            updatedAccounts.add(editAccount);
            //them tai khoan vao danh sach
        } else {
            updatedAccounts = new ArrayList<>();
            //account ton tai thì tao arraylist moi va them tai khoan vao
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(editAccount.getAccountNumber())) {
                    updatedAccounts.add(editAccount);
                } else {
                    updatedAccounts.add(account);
                }
            }
        }
        save(updatedAccounts);
    }
}
