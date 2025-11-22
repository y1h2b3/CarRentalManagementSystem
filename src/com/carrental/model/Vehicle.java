package com.carrental.model;

/**
 * 车辆父类
 */
public class Vehicle {
    private int id;              // 车辆ID
    private String type;         // 类型（客车、轿车、面包车）
    private String brand;        // 品牌（如宇通、金龙、宝马等）
    private String model;        // 型号（如6118、3系等）
    private double dailyRent;    // 日租金
    private boolean isRented;    // 是否已租出

    public Vehicle() {
    }

    public Vehicle(int id, String type, String brand, String model, double dailyRent) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.dailyRent = dailyRent;
        this.isRented = false; // 初始状态为未租出
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getDailyRent() {
        return dailyRent;
    }

    public void setDailyRent(double dailyRent) {
        this.dailyRent = dailyRent;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", dailyRent=" + dailyRent +
                ", isRented=" + isRented +
                '}';
    }
}