package com.carrental.repository;

import com.carrental.model.RentalRecord;

import java.util.List;
import java.util.Optional;

/**
 * 租赁记录数据访问接口
 */
public interface RentalRecordRepository {
    /**
     * 添加租赁记录
     * @param record 租赁记录对象
     * @return 添加是否成功
     */
    boolean addRentalRecord(RentalRecord record);

    /**
     * 更新租赁记录（主要用于归还车辆）
     * @param record 租赁记录对象
     * @return 更新是否成功
     */
    boolean updateRentalRecord(RentalRecord record);

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
     * 获取下一个可用的记录ID
     * @return 下一个可用的记录ID
     */
    int getNextRecordId();
    
    /**
     * 从文件加载租赁记录数据
     */
    void loadRentalRecords();
    
    /**
     * 将租赁记录数据保存到文件
     */
    void saveRentalRecords();
}