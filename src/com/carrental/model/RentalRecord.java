package com.carrental.model;

import java.util.Date;

/**
 *
 *
 *
 */
public class RentalRecord {
    private int id;              // 记录ID
    private Vehicle vehicle;     // 租赁的车辆
    private Customer customer;   // 客户
    private Date rentalDate;     // 租赁日期
    private Date returnDate;     // 归还日期
    private int rentalDays;      // 租赁天数
    private double totalRent;    // 总租金
    private boolean isReturned;  // 是否已归还

    public RentalRecord() {
    }

    public RentalRecord(int id, Vehicle vehicle, Customer customer, Date rentalDate, int rentalDays, double totalRent) {
        this.id = id;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.rentalDays = rentalDays;
        this.totalRent = totalRent;
        this.isReturned = false; // 初始状态为未归还
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public double getTotalRent() {
        return totalRent;
    }

    public void setTotalRent(double totalRent) {
        this.totalRent = totalRent;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    @Override
    public String toString() {
        return "RentalRecord{" +
                "id=" + id +
                ", vehicle=" + vehicle +
                ", customer=" + customer +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", rentalDays=" + rentalDays +
                ", totalRent=" + totalRent +
                ", isReturned=" + isReturned +
                '}';
    }
}