package ASM4.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService {
    public static <T> List<T> readFile(String fileName) {
       // hàm đọc file và trả về list của đối tượng
        List<T> objects = new ArrayList<>();
        //tạo list đối tượng
        try(ObjectInputStream file= new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))){
            boolean eof = false;
            //eof: end of file
            while (!eof){
                try {
                    T object = (T) file.readObject();
                    // đọc file
                    objects.add(object);
                    // thêm đối tượng vào list ban đầu
                } catch (IOException e) {
                    eof= true;
                }
            }
        }catch (EOFException e ){
            return new ArrayList<>()    ;
        } catch (IOException io) {
            System.out.println("IO Exception" + io.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException :" +e.getMessage());
        }
        return objects;
    }

    public static <T> void writeFile(String fileName, List <T> objects){
        try(ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))){
        // mở file
            for(T object : objects){
                file.writeObject(object);
                //ghi file
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
