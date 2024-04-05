package ASM4.model;

import java.io.Serializable;

public class User implements Serializable {
    private  String name;
    private  String customerId;

    public User(String customerId, String name) {
        // tạo constructor của User
        this.name = name;
        this.customerId = customerId;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
