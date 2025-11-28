package com.carrental.repository.impl;

import com.carrental.model.Bus;
import com.carrental.model.Car;
import com.carrental.model.Van;
import com.carrental.model.Vehicle;
import com.carrental.repository.VehicleRepository;
import com.carrental.util.Constants;
import com.carrental.util.FileUtil;

import java.util.*;

/**
 * 车辆数据访问实现类，使用文件存储车辆数据
 */
public class VehicleRepositoryFileImpl implements VehicleRepository {
    private List<Vehicle> vehicles; // 车辆列表

    /**
     * 构造方法，初始化车辆列表并加载文件中的数据
     */
    public VehicleRepositoryFileImpl() {
        this.vehicles = new ArrayList<>();
        loadVehicles();
        
        // 如果没有车辆数据，添加一些示例车辆
        if (vehicles.isEmpty()) {
            addSampleVehicles();
        }
    }

    /**
     * 添加示例车辆数据
     */
    private void addSampleVehicles() {
        Car car1 = new Car();
        car1.setType("轿车");
        car1.setBrand("丰田");
        car1.setModel("卡罗拉");
        car1.setDailyRent(200);
        car1.setTransmission("自动");
        addVehicle(car1);
        
        Car car2 = new Car();
        car2.setType("轿车");
        car2.setBrand("本田");
        car2.setModel("思域");
        car2.setDailyRent(220);
        car2.setTransmission("手动");
        addVehicle(car2);
        
        Car car3 = new Car();
        car3.setType("轿车");
        car3.setBrand("大众");
        car3.setModel("帕萨特");
        car3.setDailyRent(260);
        car3.setTransmission("自动");
        addVehicle(car3);
        
        // 添加一些示例面包车
        Van van1 = new Van();
        van1.setType("面包车");
        van1.setBrand("福田");
        van1.setModel("风景");
        van1.setDailyRent(300);
        van1.setLoadCapacity(2.5);
        addVehicle(van1);
        
        Van van2 = new Van();
        van2.setType("面包车");
        van2.setBrand("金杯");
        van2.setModel("海狮");
        van2.setDailyRent(320);
        van2.setLoadCapacity(3.0);
        addVehicle(van2);
        
        // 添加一些示例巴士
        Bus bus1 = new Bus();
        bus1.setType("巴士");
        bus1.setBrand("宇通");
        bus1.setModel("ZK6122HQ");
        bus1.setDailyRent(500);
        bus1.setSeats(45);
        addVehicle(bus1);
        
        Bus bus2 = new Bus();
        bus2.setType("巴士");
        bus2.setBrand("金龙");
        bus2.setModel("XMQ6118AY");
        bus2.setDailyRent(450);
        bus2.setSeats(40);
        addVehicle(bus2);
    }

    /**
     * 添加车辆
     * @param vehicle 车辆对象
     * @return 添加是否成功
     */
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        // 设置车辆ID
        vehicle.setId(getNextVehicleId());
        vehicles.add(vehicle);
        saveVehicles(); // 自动保存到文件
        return true;
    }

    /**
     * 删除车辆
     * @param id 车辆ID
     * @return 删除是否成功
     */
    @Override
    public boolean deleteVehicle(int id) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == id) {
                vehicles.remove(i);
                saveVehicles(); // 自动保存到文件
                return true;
            }
        }
        return false;
    }

    /**
     * 更新车辆信息
     * @param updatedVehicle 车辆对象
     * @return 更新是否成功
     */
    @Override
    public boolean updateVehicle(Vehicle updatedVehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId() == updatedVehicle.getId()) {
                vehicles.set(i, updatedVehicle);//替换
                saveVehicles(); // 自动保存到文件
                return true;
            }
        }
        return false;
    }

    /**
     * 根据ID查询车辆
     * @param id 车辆ID
     * @return 车辆对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<Vehicle> findVehicleById(int id) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getId() == id)
                .findFirst();
    }

    /**
     * 查询所有车辆
     * @return 所有车辆列表
     */
    @Override
    public List<Vehicle> findAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    /**
     * 根据车辆类型查询车辆
     * @param type 车辆类型
     * @return 对应类型的车辆列表
     */
    @Override
    public List<Vehicle> findVehiclesByType(String type) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getType().equalsIgnoreCase(type))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询可租赁的车辆
     * @return 可租赁车辆列表
     */
    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicles.stream()
                .filter(vehicle -> !vehicle.isRented())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询可租赁的特定类型车辆
     * @param type 车辆类型
     * @return 可租赁的特定类型车辆列表
     */
    @Override
    public List<Vehicle> findAvailableVehiclesByType(String type) {
        return vehicles.stream()
                .filter(vehicle -> !vehicle.isRented() && vehicle.getType().equalsIgnoreCase(type))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取下一个可用的车辆ID
     * @return 下一个可用的车辆ID
     */
    @Override
    public int getNextVehicleId() {
        // 如果没有车辆，返回1
        if (vehicles.isEmpty()) {
            return 1;
        }
        
        // 找到最大的ID，然后加1
        int maxId = vehicles.stream()
                .mapToInt(Vehicle::getId)
                .max()              //避免集合为空时出现 NullPointerException
                .orElse(0);   //如果 vehicles 为空（OptionalInt 无值），则返回默认值 0
        
        return maxId + 1;
    }

    /**
     * 从文件加载车辆数据
     */
    @Override
    public void loadVehicles() {
        List<String> lines = FileUtil.readLines(Constants.VEHICLE_FILE_PATH);
        vehicles.clear();
        
        for (String line : lines) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                continue;
            }
            
            // 解析车辆数据
            // 格式：ID,类型,品牌型号,购买年份,购买价格,日租金,座位数,状态
            String[] parts = line.split(",");
            if (parts.length >= 8) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    String type = parts[1];
                    String brandModel = parts[2];
                    double dailyRent = Double.parseDouble(parts[5]);
                    int seatingCapacity = Integer.parseInt(parts[6]);
                    boolean available = Boolean.parseBoolean(parts[7]);
                    
                    Vehicle vehicle;
                    // 根据类型创建不同的车辆对象
                    switch (type) {
                        case "轿车":
                            vehicle = new Car();
                            // 简单拆分品牌和型号
                            String[] brandModelParts = brandModel.split(" ", 2);
                            if (brandModelParts.length > 0) vehicle.setBrand(brandModelParts[0]);
                            if (brandModelParts.length > 1) vehicle.setModel(brandModelParts[1]);
                            vehicle.setType(type);
                            vehicle.setDailyRent(dailyRent);
                            ((Car)vehicle).setTransmission("自动"); // 默认设置
                            break;
                        case "面包车":
                            vehicle = new Van();
                            brandModelParts = brandModel.split(" ", 2);
                            if (brandModelParts.length > 0) vehicle.setBrand(brandModelParts[0]);
                            if (brandModelParts.length > 1) vehicle.setModel(brandModelParts[1]);
                            vehicle.setType(type);
                            vehicle.setDailyRent(dailyRent);
                            ((Van)vehicle).setLoadCapacity(2.5); // 默认设置
                            break;
                        case "巴士":
                        case "客车":
                            vehicle = new Bus();
                            brandModelParts = brandModel.split(" ", 2);
                            if (brandModelParts.length > 0) vehicle.setBrand(brandModelParts[0]);
                            if (brandModelParts.length > 1) vehicle.setModel(brandModelParts[1]);
                            vehicle.setType(type);
                            vehicle.setDailyRent(dailyRent);
                            ((Bus)vehicle).setSeats(seatingCapacity);
                            break;
                        default:
                            continue; // 未知类型，跳过
                    }
                    
                    vehicle.setId(id);
                    vehicle.setRented(!available); // isAvailable()对应!isRented()
                    vehicles.add(vehicle);
                    
                } catch (NumberFormatException e) {
                    System.err.println("解析车辆数据失败: " + line);
                }
            }
        }
    }

    /**
     * 将车辆数据保存到文件
     */
    @Override
    public void saveVehicles() {
        List<String> lines = new ArrayList<>();
        
        // 将所有车辆转换为字符串格式存入
        for (Vehicle vehicle : vehicles) {
            String brandModel = vehicle.getBrand() + " " + vehicle.getModel();
            //座位默认
            int seatingCapacity = 0;
            if (vehicle instanceof Bus) {
                seatingCapacity = ((Bus)vehicle).getSeats();
            } else if (vehicle instanceof Van) {
                seatingCapacity = 9; // 默认值
            } else if (vehicle instanceof Car) {
                seatingCapacity = 5; // 默认值
            }
            
            String line = String.format("%d,%s,%s,0,0,%.2f,%d,%b",
                    vehicle.getId(),
                    vehicle.getType(),
                    brandModel,
                    vehicle.getDailyRent(),
                    seatingCapacity,
                    !vehicle.isRented()); // isAvailable()对应!isRented()
            lines.add(line);
        }
        
        // 写入文件
        FileUtil.writeLines(Constants.VEHICLE_FILE_PATH, lines);
    }
}