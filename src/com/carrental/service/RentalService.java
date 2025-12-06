package com.carrental.service;

import com.carrental.model.RentalRecord;

import java.util.List;
import java.util.Optional;

/**
 * 租赁服务接口，定义租赁相关的业务逻辑
 */
public interface RentalService {
    /**
     * 租车
     * @param vehicleId 车辆ID
     * @param customerId 客户ID
     * @param rentalDays 租赁天数
     * @return 租赁记录
     */
    Optional<RentalRecord> rentVehicle(int vehicleId, int customerId, int rentalDays);

    /**
     * 还车
     * @param recordId 租赁记录ID
     * @return 租赁记录
     */
    Optional<RentalRecord> returnVehicle(int recordId);

    /**
     * 根据ID查询租赁记录
     * @param id 记录ID
     * @return 租赁记录对象，如果不存在则返回Optional.empty()
     */
    Optional<RentalRecord> findRentalRecordById(int id);

    /**
     * 查询所有租赁记录
     * @return 所有租赁记录列表
     */
    List<RentalRecord> findAllRentalRecords();

    /**
     * 查询未归还的租赁记录
     * @return 未归还的租赁记录列表
     */
    List<RentalRecord> findUnreturnedRentalRecords();

    /**
     * 根据车辆ID查询租赁记录
     * @param vehicleId 车辆ID
     * @return 该车辆的租赁记录列表
     */
    List<RentalRecord> findRentalRecordsByVehicleId(int vehicleId);

    /**
     * 根据客户ID查询租赁记录
     * @param customerId 客户ID
     * @return 该客户的租赁记录列表
     */
    List<RentalRecord> findRentalRecordsByCustomerId(int customerId);

    /**
     * 计算租金
     * @param vehicleId 车辆ID
     * @param customerId 客户ID
     * @param rentalDays 租赁天数
     * @return 计算后的租金
     */
    double calculateRent(int vehicleId, int customerId, int rentalDays);
}