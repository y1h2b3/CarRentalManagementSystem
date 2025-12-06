package com.carrental.service.Impl;

import com.carrental.model.Vehicle;
import com.carrental.repository.VehicleRepository;
import com.carrental.service.VehicleService;

import java.util.List;
import java.util.Optional;

/**
 * 车辆服务实现类
 */
public class VehicleServiceImpl implements VehicleService {
    private VehicleRepository vehicleRepository;

    // 构造函数注入依赖
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    /**
     * 添加车辆
     * @param vehicle 车辆对象
     * @return 添加是否成功
     */
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        return vehicleRepository.addVehicle(vehicle);
    }
    /**
     * 删除车辆
     * @param id 车辆ID
     * @return 删除是否成功
     */
    @Override
    public boolean deleteVehicle(int id) {
        return vehicleRepository.deleteVehicle(id);
    }
    /**
     * 更新车辆信息
     * @param vehicle 车辆对象
     * @return 更新是否成功
     */
    @Override
    public boolean updateVehicle(Vehicle vehicle) {
        return vehicleRepository.updateVehicle(vehicle);
    }
    /**
     * 根据ID查询车辆
     * @param id 车辆ID
     * @return 车辆对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<Vehicle> findVehicleById(int id) {
        return vehicleRepository.findVehicleById(id);
    }
    /**
     * 查询所有车辆
     * @return 所有车辆列表
     */
    @Override
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAllVehicles();
    }
    /**
     * 根据车辆类型查询车辆
     * @param type 车辆类型
     * @return 对应类型的车辆列表
     */
    @Override
    public List<Vehicle> findVehiclesByType(String type) {
        return vehicleRepository.findVehiclesByType(type);
    }
    /**
      * 查询可租赁的车辆
      * @return 可租赁车辆列表
      */
//   @Override
//    public List<Vehicle> findAvailableVehicles() {
//        return vehicleRepository.findAvailableVehicles();
//    }
    /**
     * 查询可租赁的特定类型车辆
     * @param type 车辆类型
     * @return 可租赁的特定类型车辆列表
     */
    @Override
    public List<Vehicle> findAvailableVehiclesByType(String type) {
        return vehicleRepository.findAvailableVehiclesByType(type);
    }

    /**
     * 将车辆标记为已出租
     * @param id 车辆ID
     * @return 是否标记成功
     */
    @Override
    public boolean markVehicleAsRented(int id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findVehicleById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            if (!vehicle.isRented()) {
                vehicle.setRented(true);
                return vehicleRepository.updateVehicle(vehicle);
            }
        }
        return false;
    }
    /**
     * 将车辆标记为可出租
     * @param id 车辆ID
     * @return 是否标记成功
     */
    @Override
    public boolean markVehicleAsAvailable(int id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findVehicleById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            if (vehicle.isRented()) {
                vehicle.setRented(false);
                return vehicleRepository.updateVehicle(vehicle);
            }
        }
        return false;
    }
}