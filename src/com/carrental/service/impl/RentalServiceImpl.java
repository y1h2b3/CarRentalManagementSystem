package com.carrental.service.impl;

import com.carrental.model.Customer;
import com.carrental.model.RentalRecord;
import com.carrental.model.Vehicle;
import com.carrental.repository.RentalRecordRepository;
import com.carrental.service.CustomerService;
import com.carrental.service.RentalService;
import com.carrental.service.VehicleService;
import com.carrental.util.Constants;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 租赁服务实现类
 */
public class RentalServiceImpl implements RentalService {
    private VehicleService vehicleService;
    private CustomerService customerService;
    private RentalRecordRepository rentalRecordRepository;

    // 构造函数注入依赖
    public RentalServiceImpl(VehicleService vehicleService, CustomerService customerService, RentalRecordRepository rentalRecordRepository) {
        this.vehicleService = vehicleService;
        this.customerService = customerService;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    /**
     * 租车
     * @param vehicleId 车辆ID
     * @param customerId 客户ID
     * @param rentalDays 租赁天数
     * @return 租赁记录
     */
    @Override
    public Optional<RentalRecord> rentVehicle(int vehicleId, int customerId, int rentalDays) {
        // 检查车辆是否存在且可租赁
        Optional<Vehicle> optionalVehicle = vehicleService.findVehicleById(vehicleId);
        if (!optionalVehicle.isPresent() || optionalVehicle.get().isRented()) {
            System.out.println("车辆不存在或已被出租！");
            return Optional.empty();
        }

        // 检查客户是否存在
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);
        if (!optionalCustomer.isPresent()) {
            System.out.println("客户不存在！");
            return Optional.empty();
        }

        // 计算租金
        double totalRent = calculateRent(vehicleId, customerId, rentalDays);

        // 更新车辆状态为已出租
        if (!vehicleService.markVehicleAsRented(vehicleId)) {
            System.out.println("更新车辆状态失败！");
            return Optional.empty();
        }

        // 创建租赁记录
        RentalRecord rentalRecord = new RentalRecord(
                rentalRecordRepository.getNextRecordId(),
                optionalVehicle.get(),
                optionalCustomer.get(),
                new Date(),
                rentalDays,
                totalRent
        );

        // 保存租赁记录
        if (!rentalRecordRepository.addRentalRecord(rentalRecord)) {
            System.out.println("创建租赁记录失败！");
            // 回滚车辆状态
            vehicleService.markVehicleAsAvailable(vehicleId);
            return Optional.empty();
        }

        return Optional.of(rentalRecord);
    }

    /**
     * 还车
     * @param recordId 租赁记录ID
     * @return 租赁记录
     */
    @Override
    public Optional<RentalRecord> returnVehicle(int recordId) {
        // 检查租赁记录是否存在
        Optional<RentalRecord> optionalRecord = rentalRecordRepository.findRentalRecordById(recordId);
        if (!optionalRecord.isPresent()) {
            System.out.println("租赁记录不存在！");
            return Optional.empty();
        }

        RentalRecord rentalRecord = optionalRecord.get();

        // 检查车辆是否已经归还
        if (rentalRecord.isReturned()) {
            System.out.println("车辆已经归还！");
            return Optional.empty();
        }

        // 更新车辆状态为可出租
        int vehicleId = rentalRecord.getVehicle().getId();
        if (!vehicleService.markVehicleAsAvailable(vehicleId)) {
            System.out.println("更新车辆状态失败！");
            return Optional.empty();
        }

        // 更新租赁记录
        rentalRecord.setReturnDate(new Date());
        rentalRecord.setReturned(true);

        if (!rentalRecordRepository.updateRentalRecord(rentalRecord)) {
            System.out.println("更新租赁记录失败！");
            // 回滚车辆状态
            vehicleService.markVehicleAsRented(vehicleId);
            return Optional.empty();
        }

        return Optional.of(rentalRecord);
    }

    /**
     * 根据ID查询租赁记录
     * @param id 记录ID
     * @return 租赁记录对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<RentalRecord> findRentalRecordById(int id) {
        return rentalRecordRepository.findRentalRecordById(id);
    }

    /**
     * 查询所有租赁记录
     * @return 所有租赁记录列表
     */
    @Override
    public List<RentalRecord> findAllRentalRecords() {
        return rentalRecordRepository.findAllRentalRecords();
    }

    /**
     * 查询未归还的租赁记录
     * @return 未归还的租赁记录列表
     */
    @Override
    public List<RentalRecord> findUnreturnedRentalRecords() {
        return rentalRecordRepository.findUnreturnedRentalRecords();
    }

    /**
     * 根据车辆ID查询租赁记录
     * @param vehicleId 车辆ID
     * @return 该车辆的租赁记录列表
     */
    @Override
    public List<RentalRecord> findRentalRecordsByVehicleId(int vehicleId) {
        return rentalRecordRepository.findRentalRecordsByVehicleId(vehicleId);
    }
    /**
     * 根据客户ID查询租赁记录
     * @param customerId 客户ID
     * @return 该客户的租赁记录列表
     */
    @Override
    public List<RentalRecord> findRentalRecordsByCustomerId(int customerId) {
        return rentalRecordRepository.findRentalRecordsByCustomerId(customerId);
    }

    /**
     * 计算租金
     * @param vehicleId 车辆ID
     * @param customerId 客户ID
     * @param rentalDays 租赁天数
     * @return 计算后的租金
     */
    @Override
    public double calculateRent(int vehicleId, int customerId, int rentalDays) {
        Optional<Vehicle> optionalVehicle = vehicleService.findVehicleById(vehicleId);
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerId);

        if (!optionalVehicle.isPresent() || !optionalCustomer.isPresent()) {
            return 0.0;
        }

        Vehicle vehicle = optionalVehicle.get();
        Customer customer = optionalCustomer.get();

        // 获取基础租金
        double baseRent = vehicle.getDailyRent() * rentalDays;

        // 根据客户类型应用折扣
        double discount = getDiscountByCustomerType(customer.getCustomerType());

        return baseRent * discount;
    }

    /**
     * 根据客户类型获取折扣率
     * @param customerType 客户类型
     * @return 折扣率
     */
    private double getDiscountByCustomerType(String customerType) {
        switch (customerType) {
            case Constants.CUSTOMER_TYPE_REGULAR:
                return Constants.DISCOUNT_REGULAR;
            case Constants.CUSTOMER_TYPE_VIP:
                return Constants.DISCOUNT_VIP;
            case Constants.CUSTOMER_TYPE_ENTERPRISE:
                return Constants.DISCOUNT_ENTERPRISE;
            default:
                return Constants.DISCOUNT_REGULAR;
        }
    }
}