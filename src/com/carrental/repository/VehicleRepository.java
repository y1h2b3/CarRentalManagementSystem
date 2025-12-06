package com.carrental.repository;

import com.carrental.model.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * 车辆数据访问接口
 */
public interface VehicleRepository {
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
        Optional <Vehicle> findVehicleById(int id);

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

        /**
         * 查询可租赁的车辆
         * @return 可租赁车辆列表
         */
        List<Vehicle> findAvailableVehicles();

        /**
         * 查询可租赁的特定类型车辆
         * @param type 车辆类型
         * @return 可租赁的特定类型车辆列表
         */
        List<Vehicle> findAvailableVehiclesByType(String type);

        /**
         * 获取下一个可用的车辆ID
         * @return 下一个可用的车辆ID
         */
        int getNextVehicleId();

        /**
         * 从文件加载车辆数据
         */
        void loadVehicles();

        /**
         * 将车辆数据保存到文件
         */
        void saveVehicles();
}