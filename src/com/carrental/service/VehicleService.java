package com.carrental.service;

import com.carrental.model.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * 车辆服务接口，定义车辆相关的业务逻辑
 */
public interface VehicleService {
    /**
     * 添加车辆
     * @param vehicle 车辆对象
     * @return 添加是否成功
     */
    boolean addVehicle(Vehicle vehicle);

    /**
     * 删除车辆
     * @param id 车辆ID
     * @return 删除是否成功
     */
    boolean deleteVehicle(int id);

    /**
     * 更新车辆信息
     * @param vehicle 车辆对象
     * @return 更新是否成功
     */
    boolean updateVehicle(Vehicle vehicle);

    /**
     * 根据ID查询车辆
     * @param id 车辆ID
     * @return 车辆对象，如果不存在则返回Optional.empty()
     */
    Optional<Vehicle> findVehicleById(int id);

    /**
     * 查询所有车辆
     * @return 所有车辆列表
     */
    List<Vehicle> findAllVehicles();

    /**
     * 根据车辆类型查询车辆
     * @param type 车辆类型
     * @return 对应类型的车辆列表
     */
    List<Vehicle> findVehiclesByType(String type);

//    /**
//     * 查询可租赁的车辆
//     * @return 可租赁车辆列表
//     */
//    List<Vehicle> findAvailableVehicles();

    /**
     * 查询可租赁的特定类型车辆
     * @param type 车辆类型
     * @return 可租赁的特定类型车辆列表
     */
    List<Vehicle> findAvailableVehiclesByType(String type);

    /**
     * 将车辆标记为已出租
     * @param id 车辆ID
     * @return 是否标记成功
     */
    boolean markVehicleAsRented(int id);

    /**
     * 将车辆标记为可出租
     * @param id 车辆ID
     * @return 是否标记成功
     */
    boolean markVehicleAsAvailable(int id);
}