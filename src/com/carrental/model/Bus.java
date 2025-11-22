package com.carrental.model;

/**
 * 客车类
 */
public class Bus extends Vehicle {
    // 客车特有的属性可以在这里添加
    // 例如：座位数、行李舱大小等
    private int seats; // 座位数

    public Bus() {
    }

    public Bus(int id, String brand, String model, double dailyRent, int seats) {
        super(id, "客车", brand, model, dailyRent);
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + getId() +
                ", type='" + getType() + '\'' +
                ", brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", dailyRent=" + getDailyRent() +
                ", isRented=" + isRented() +
                ", seats=" + seats +
                '}';
    }
}