package com.carrental.controller;

import com.carrental.model.Customer;
import com.carrental.model.RentalRecord;
import com.carrental.model.Vehicle;
import com.carrental.service.CustomerService;
import com.carrental.service.RentalService;
import com.carrental.service.VehicleService;
import com.carrental.util.InputUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * 日常业务管理控制器，处理租车和还车相关的用户交互
 */
public class RentalController {
    private RentalService rentalService;
    private VehicleService vehicleService;
    private CustomerService customerService;
    private VehicleController vehicleController;
    private CustomerController customerController;

    // 构造函数注入依赖
    public RentalController(RentalService rentalService, VehicleService vehicleService, CustomerService customerService, 
                          VehicleController vehicleController, CustomerController customerController) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.vehicleController = vehicleController;
        this.customerController = customerController;
    }

    /**
     * 显示日常业务管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 日常业务管理 =====");
            System.out.println("1. 租车");
            System.out.println("2. 还车");
            System.out.println("3. 查看未归还记录");
            System.out.println("4. 查看所有租赁记录");
            System.out.println("5. 查看客户租赁记录");
            System.out.println("0. 返回上一级");

            int choice = InputUtil.getInt("请选择操作: ");

            switch (choice) {
                case 1:
                    rentVehicle();
                    break;
                case 2:
                    returnVehicle();
                    break;
                case 3:
                    displayUnreturnedRecords();
                    break;
                case 4:
                    displayAllRecords();
                    break;
                case 5:
                    displayRentalRecordsByCustomerId();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    /**
     * 租车功能
     */
    private void rentVehicle() {
        System.out.println("\n===== 租车 =====");

        String type=InputUtil.getString("选择要租的车型（轿车，面包车，巴士，客车）：");

        // 先显示可租赁车辆
        if(vehicleController.findAvailableVehiclesByType(type)) {
            int vehicleId = InputUtil.getInt("请输入要租赁的车辆ID: ");
            
            // 验证车辆是否存在且类型匹配
            Optional<Vehicle> optionalVehicle = vehicleService.findVehicleById(vehicleId);
            if (!optionalVehicle.isPresent()) {
                System.out.println("未找到ID为" + vehicleId + "的车辆！");
                return;
            }
            Vehicle vehicle = optionalVehicle.get();
            if (!vehicle.getType().equals(type)) {
                System.out.println("选择的车辆不是" + type + "类型！");
                return;
            }
            if (vehicle.isRented()) {
                System.out.println("该车辆已被出租！");
                return;
            }

            // 显示客户列表
            System.out.println("\n客户列表：");
            customerController.displayAllCustomers();

            int customerId = InputUtil.getInt("请输入客户ID: ");
            
            // 验证客户是否存在
            Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);
            if (!optionalCustomer.isPresent()) {
                System.out.println("未找到ID为" + customerId + "的客户！");
                return;
            }

            int rentalDays = InputUtil.getInt("请输入租赁天数: ");
            if (rentalDays <= 0) {
                System.out.println("租赁天数必须大于0！");
                return;
            }

            // 执行租车操作
            Optional<RentalRecord> optionalRecord = rentalService.rentVehicle(vehicleId, customerId, rentalDays);

            if (optionalRecord.isPresent()) {
                RentalRecord record = optionalRecord.get();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("\n租车成功！");
                System.out.println("租赁记录ID: " + record.getId());
                System.out.println("车辆信息: " + record.getVehicle().getType() + " " + record.getVehicle().getBrand() + " " + record.getVehicle().getModel());
                System.out.println("客户信息: " + record.getCustomer().getName() + " (" + record.getCustomer().getCustomerType() + ")");
                System.out.println("租赁日期: " + sdf.format(record.getRentalDate()));
                System.out.println("租赁天数: " + record.getRentalDays());
                System.out.println("总租金: " + record.getTotalRent() + " 元");
                System.out.println("请妥善保存租赁记录ID，用于还车时使用。");
            } else {
                System.out.println("租车失败！");
            }
        }

    }

    /**
     * 还车功能
     */
    private void returnVehicle() {
        System.out.println("\n===== 还车 =====");
        
        // 先显示未归还的租赁记录
        System.out.println("未归还的租赁记录：");
        displayUnreturnedRecords();
        
        int recordId = InputUtil.getInt("请输入租赁记录ID: ");
        
        // 执行还车操作
        Optional<RentalRecord> optionalRecord = rentalService.returnVehicle(recordId);
        
        if (optionalRecord.isPresent()) {
            RentalRecord record = optionalRecord.get();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("\n还车成功！");
            System.out.println("租赁记录ID: " + record.getId());
            System.out.println("车辆信息: " + record.getVehicle().getType() + " " + record.getVehicle().getBrand() + " " + record.getVehicle().getModel());
            System.out.println("客户信息: " + record.getCustomer().getName());
            System.out.println("租赁日期: " + sdf.format(record.getRentalDate()));
            System.out.println("归还日期: " + sdf.format(record.getReturnDate()));
            System.out.println("租赁天数: " + record.getRentalDays());
            System.out.println("总租金: " + record.getTotalRent() + " 元");
        } else {
            System.out.println("还车失败！");
        }
    }

    /**
     * 显示所有租赁记录
     */
    private void displayAllRecords() {
        System.out.println("\n===== 所有租赁记录 =====");
        List<RentalRecord> records = rentalService.findAllRentalRecords();
        displayRentalRecords(records);
    }

    /**
     * 显示未归还的租赁记录
     */
    private void displayUnreturnedRecords() {
        System.out.println("\n===== 未归还的租赁记录 =====");
        List<RentalRecord> records = rentalService.findUnreturnedRentalRecords();
        displayRentalRecords(records);
    }

    /**
     * 显示租赁记录列表
     * @param records 租赁记录列表
     */
    private void displayRentalRecords(List<RentalRecord> records) {
        if (records.isEmpty()) {
            System.out.println("没有租赁记录！");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.printf("%-5s %-20s %-10s %-15s %-20s %-20s %-10s %-10s\n", 
                "ID", "车辆信息", "客户", "租赁日期", "归还日期", "租赁天数", "总租金", "状态");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        for (RentalRecord record : records) {
            System.out.printf("%-5d %-20s %-10s %-15s %-20s %-10d %-10.2f %-10s\n",
                    record.getId(),
                    record.getVehicle().getType() + " " + record.getVehicle().getBrand() + " " + record.getVehicle().getModel(),
                    record.getCustomer().getName(),
                    sdf.format(record.getRentalDate()),
                    record.getReturnDate() != null ? sdf.format(record.getReturnDate()) : "-",
                    record.getRentalDays(),
                    record.getTotalRent(),
                    record.isReturned() ? "已归还" : "未归还");
        }
    }

    /**
     * 显示特定客户的租赁记录
     */
    private void displayRentalRecordsByCustomerId() {
        System.out.println("\n===== 查看客户租赁记录 =====");
        
        // 先显示客户列表
        System.out.println("客户列表：");
        customerController.displayAllCustomers();
        
        int customerId = InputUtil.getInt("请输入客户ID: ");
        
        // 验证客户是否存在
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);
        if (!optionalCustomer.isPresent()) {
            System.out.println("未找到ID为" + customerId + "的客户！");
            return;
        }
        
        // 查询并显示该客户的租赁记录
        List<RentalRecord> records = rentalService.findRentalRecordsByCustomerId(customerId);
        System.out.println("\n===== 客户'" + optionalCustomer.get().getName() + "'的租赁记录 =====");
        displayRentalRecords(records);
    }
}