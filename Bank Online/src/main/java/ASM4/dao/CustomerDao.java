package ASM4.dao;

import ASM4.model.Customer;
import ASM4.service.BinaryFileService;
import java.io.IOException;
import java.util.List;

public class CustomerDao {
    private final static String FILE_PATH = "store/customers.dat";
    public static void save(List<Customer> customers){
        //lưu list vào file
        BinaryFileService.writeFile(FILE_PATH, customers);
    }
    public static List<Customer> customersList() {
        return BinaryFileService.readFile(FILE_PATH);
    }


}
