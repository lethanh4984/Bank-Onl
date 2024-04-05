package ASM4.dao;

import ASM4.model.Transaction;
import ASM4.service.BinaryFileService;

import java.io.IOException;
import java.util.List;

public class TransactionDao {
    private final static String FILE_PATH = "store/transactions.dat";
    public static void save(List<Transaction> transactions) throws IOException {
        //lưu list vào file
        BinaryFileService.writeFile(FILE_PATH, transactions);
    }
    public static List<Transaction> transactionList() {
        return BinaryFileService.readFile(FILE_PATH);
    }
}
