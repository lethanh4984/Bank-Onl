package ASM4.file;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextFileService {

    public static final String COMMA_DELIMITER = ",";
        public static List<List<String>> readFile(Path fileName){
            List<List<String>> customerList= new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName.toFile()))){
                //try with resources đóng file tự động và không cần đóng file
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> list= List.of(line.split(COMMA_DELIMITER));
                    customerList.add(list);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return customerList ;
        //hoặc dùng split của string để cắt ra id và name
        }
}
