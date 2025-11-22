package com.carrental.model;

/**
 * 面包车类
 */
public class Van extends Vehicle {
    // 面包车特有的属性可以在这里添加
    // 例如：载重、容积等
    private double loadCapacity; // 载重（吨）

    public Van() {
    }

    public Van(int id, String brand, String model, double dailyRent, double loadCapacity) {
        super(id, "面包车", brand, model, dailyRent);
        this.loadCapacity = loadCapacity;
    }

    public double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    @Override
    public String toString() {
        return "Van{" +
                "id=" + getId() +
                ", type='" + getType() + '\'' +
                ", brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", dailyRent=" + getDailyRent() +
                ", isRented=" + isRented() +
                ", loadCapacity=" + loadCapacity +
                '}';
    }
}