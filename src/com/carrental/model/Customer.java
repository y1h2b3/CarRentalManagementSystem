package com.carrental.model;

/**
 * 客户类
 */
public class Customer {
    private int id;          // 客户ID
    private String name;     // 姓名
    private String phone;    // 电话
    private String customerType; // 客户类型（普通、VIP、企业）

    public Customer() {
    }

    public Customer(int id, String name, String phone, String customerType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.customerType = customerType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", customerType='" + customerType + '\'' +
                '}';
    }
}