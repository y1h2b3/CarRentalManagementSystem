package com.carrental.model;

/**
 * 轿车类
 */
public class Car extends Vehicle {
    // 轿车特有的属性可以在这里添加
    // 例如：排量、变速箱类型等
    private String transmission; // 变速箱类型（自动/手动）

    public Car() {
    }

    public Car(int id, String brand, String model, double dailyRent, String transmission) {
        super(id, "轿车", brand, model, dailyRent);
        this.transmission = transmission;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + getId() +
                ", type='" + getType() + '\'' +
                ", brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", dailyRent=" + getDailyRent() +
                ", isRented=" + isRented() +
                ", transmission='" + transmission + '\'' +
                '}';
    }
}