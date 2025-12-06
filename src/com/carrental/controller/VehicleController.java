package com.carrental.controller;

import com.carrental.model.Vehicle;
import com.carrental.service.VehicleService;
import com.carrental.util.Constants;
import com.carrental.util.InputUtil;

import java.util.List;
import java.util.Optional;

/**
 * 汽车信息管理控制器，处理汽车相关的用户交互
 */
public class VehicleController {
    private VehicleService vehicleService;

    // 构造函数注入依赖
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * 显示汽车信息管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 汽车信息管理 =====");
            System.out.println("1. 客车信息管理");
            System.out.println("2. 轿车信息管理");
            System.out.println("3. 面包车信息管理");
            System.out.println("4. 查看所有车辆");
            System.out.println("0. 返回上一级");

            int choice = InputUtil.getInt("请选择操作: ");

            switch (choice) {
                case 1:
                    manageVehiclesByType(Constants.VEHICLE_TYPE_BUS);
                    break;
                case 2:
                    manageVehiclesByType(Constants.VEHICLE_TYPE_CAR);
                    break;
                case 3:
                    manageVehiclesByType(Constants.VEHICLE_TYPE_VAN);
                    break;
                case 4:
                    displayAllVehicles();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    /**
     * 按车辆类型管理车辆
     * @param type 车辆类型
     */
    private void manageVehiclesByType(String type) {
        while (true) {
            System.out.println("\n===== " + type + "信息管理 =====");
            System.out.println("1. 添加" + type);
            System.out.println("2. 修改" + type);
            System.out.println("3. 删除" + type);
            System.out.println("4. 查看" + type + "列表");
            System.out.println("0. 返回上一级");

            int choice = InputUtil.getInt("请选择操作: ");

            switch (choice) {
                case 1:
                    addVehicle(type);
                    break;
                case 2:
                    updateVehicle(type);
                    break;
                case 3:
                    deleteVehicle(type);
                    break;
                case 4:
                    displayVehiclesByType(type);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    /**
     * 添加车辆
     * @param type 车辆类型
     */
    private void addVehicle(String type) {
        System.out.println("\n===== 添加" + type + " =====");
        String brand = InputUtil.getNonEmptyString("请输入品牌: ");
        String model = InputUtil.getNonEmptyString("请输入车型: ");
        double dailyRent = InputUtil.getDouble("请输入日租金: ");

        Vehicle vehicle = new Vehicle();
        vehicle.setType(type);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setDailyRent(dailyRent);

        if (vehicleService.addVehicle(vehicle)) {
            System.out.println("添加" + type + "成功！");
        } else {
            System.out.println("添加" + type + "失败！");
        }
    }

    /**
     * 修改车辆
     * @param type 车辆类型
     */
    private void updateVehicle(String type) {
        System.out.println("\n===== 修改" + type + " =====");
        displayVehiclesByType(type);

        int id = InputUtil.getInt("请输入要修改的" + type + "ID: ");
        Optional<Vehicle> optionalVehicle = vehicleService.findVehicleById(id);

        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            System.out.println("当前" + type + "信息: " + vehicle);

            String brand = InputUtil.getString("请输入新品牌 (回车保持不变): ");
            if (!brand.trim().isEmpty()) {
                vehicle.setBrand(brand);
            }

            String model = InputUtil.getString("请输入新车型 (回车保持不变): ");
            if (!model.trim().isEmpty()) {
                vehicle.setModel(model);
            }

            String dailyRentStr = InputUtil.getString("请输入新日租金 (回车保持不变): ");
            if (!dailyRentStr.trim().isEmpty()) {
                try {
                    vehicle.setDailyRent(Double.parseDouble(dailyRentStr));
                } catch (NumberFormatException e) {
                    System.out.println("日租金格式错误，保持原有值！");
                }
            }

            if (vehicleService.updateVehicle(vehicle)) {
                System.out.println("修改" + type + "成功！");
            } else {
                System.out.println("修改" + type + "失败！");
            }
        } else {
            System.out.println("未找到ID为" + id + "的" + type + "！");
        }
    }

    /**
     * 删除车辆
     * @param type 车辆类型
     */
    private void deleteVehicle(String type) {
        System.out.println("\n===== 删除" + type + " =====");
        displayVehiclesByType(type);

        int id = InputUtil.getInt("请输入要删除的" + type + "ID: ");
        Optional<Vehicle> optionalVehicle = vehicleService.findVehicleById(id);

        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            if (vehicle.isRented()) {
                System.out.println("该" + type + "已被出租，无法删除！");
                return;
            }

            if (vehicleService.deleteVehicle(id)) {
                System.out.println("删除" + type + "成功！");
            } else {
                System.out.println("删除" + type + "失败！");
            }
        } else {
            System.out.println("未找到ID为" + id + "的" + type + "！");
        }
    }

    /**
     * 显示所有车辆
     */
    public void displayAllVehicles() {
        System.out.println("\n===== 所有车辆列表 =====");
        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        displayVehicles(vehicles);
    }

    /**
     * 按类型显示车辆
     * @param type 车辆类型
     */
    public void displayVehiclesByType(String type) {
        System.out.println("\n===== " + type + "列表 =====");
        List<Vehicle> vehicles = vehicleService.findVehiclesByType(type);
        displayVehicles(vehicles);
    }

    public Boolean findAvailableVehiclesByType(String type) {
        System.out.println("\n===== 可租赁的" + type + "列表 =====");
        List<Vehicle> vehicles = vehicleService.findAvailableVehiclesByType(type);
        return displayVehicles(vehicles);
    }

    /**
     * 显示车辆列表
     * @param vehicles 车辆列表
     */
    private Boolean displayVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("没有车辆记录！");
            return false;
        }

        System.out.printf("%-5s %-15s %-10s %-20s %-15s %-10s\n",
                "ID", "类型", "品牌", "车型", "日租金", "状态");
        System.out.println("-----------------------------------------------------------------------------");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("%-5d %-15s %-10s %-20s %-15.2f %-10s\n",
                    vehicle.getId(),
                    vehicle.getType(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getDailyRent(),
                    vehicle.isRented() ? "已出租" : "可出租");
        }
        return true;
    }

}