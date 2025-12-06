package com.carrental.repository.Impl;

import com.carrental.model.RentalRecord;
import com.carrental.model.Vehicle;
import com.carrental.model.Customer;
import com.carrental.repository.RentalRecordRepository;
import com.carrental.repository.VehicleRepository;
import com.carrental.repository.CustomerRepository;
import com.carrental.util.Constants;
import com.carrental.util.FileUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 租赁记录数据访问实现类，使用文件存储租赁记录数据
 */
public class RentalRecordRepositoryFileImpl implements RentalRecordRepository {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式化
    private List<RentalRecord> rentalRecords; // 租赁记录列表
    private VehicleRepository vehicleRepository; // 车辆仓库引用
    private CustomerRepository customerRepository; // 客户仓库引用

    /**
     * 构造方法，初始化租赁记录列表
     */
    public RentalRecordRepositoryFileImpl() {
        this.rentalRecords = new ArrayList<>();
    }

    /**
     * 设置车辆，客户仓库引用
     * @param vehicleRepository 车辆仓库
     * @param customerRepository 客户仓库
     */
    public void setRepository(VehicleRepository vehicleRepository,CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        // 设置依赖后重新加载数据，确保能正确关联车辆对象
        loadRentalRecords();
    }


    /**
     * 添加租赁记录
     * @param record 租赁记录对象
     * @return 添加是否成功
     */
    @Override
    public boolean addRentalRecord(RentalRecord record) {
        if (record != null) {
            // 设置记录ID
            record.setId(getNextRecordId());
            rentalRecords.add(record);
            saveRentalRecords(); // 自动保存到文件
            return true;
        }
        return false;
    }

    /**
     * 更新租赁记录（主要用于归还车辆）
     * @param record 租赁记录对象
     * @return 更新是否成功
     */
    @Override
    public boolean updateRentalRecord(RentalRecord record) {
        if (record == null) return false;

        Optional<RentalRecord> optionalRecord = findRentalRecordById(record.getId());
        if (optionalRecord.isPresent()) {
            saveRentalRecords(); // 自动保存到文件
            return true;
        }
        return false;
    }

    /**
     * 根据ID查询租赁记录
     * @param id 记录ID
     * @return 租赁记录对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<RentalRecord> findRentalRecordById(int id) {
        return rentalRecords.stream()
                .filter(record -> record.getId() == id)
                .findFirst();
    }

    /**
     * 查询所有租赁记录
     * @return 所有租赁记录列表
     */
    @Override
    public List<RentalRecord> findAllRentalRecords() {
        return new ArrayList<>(rentalRecords);
    }

    /**
     * 查询未归还的租赁记录
     * @return 未归还的租赁记录列表
     */
    @Override
    public List<RentalRecord> findUnreturnedRentalRecords() {
        return rentalRecords.stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }

    /**
     * 根据车辆ID查询租赁记录
     * @param vehicleId 车辆ID
     * @return 该车辆的租赁记录列表
     */
    @Override
    public List<RentalRecord> findRentalRecordsByVehicleId(int vehicleId) {
        return rentalRecords.stream()
                .filter(record -> record.getVehicle() != null && record.getVehicle().getId() == vehicleId)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户ID查询租赁记录
     * @param customerId 客户ID
     * @return 该客户的租赁记录列表
     */
    @Override
    public List<RentalRecord> findRentalRecordsByCustomerId(int customerId) {
        return rentalRecords.stream()
                .filter(record -> record.getCustomer() != null && record.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    /**
     * 获取下一个可用的记录ID
     * @return 下一个可用的记录ID
     */
    @Override
    public int getNextRecordId() {
        // 如果没有记录，返回1
        if (rentalRecords.isEmpty()) {
            return 1;
        }
        
        // 找到最大的ID，然后加1
        int maxId = rentalRecords.stream()
                .mapToInt(RentalRecord::getId)
                .max()
                .orElse(0);
        
        return maxId + 1;
    }

    /**
     * 从文件加载租赁记录数据
     */
    @Override
    public void loadRentalRecords() {
        List<String> lines = FileUtil.readLines(Constants.RENTAL_FILE_PATH);
        rentalRecords.clear();
        
        for (String line : lines) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                continue;
            }
            
            try {
                // 解析租赁记录数据
                // 格式：ID,车辆ID,客户ID,租赁日期,归还日期,总租金,状态
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    int id = Integer.parseInt(parts[0]);
                    int vehicleId = Integer.parseInt(parts[1]);
                    int customerId = Integer.parseInt(parts[2]);
                    Date rentalDate = DATE_FORMAT.parse(parts[3]);
                    Date returnDate = parts[4].equals("null") ? null : DATE_FORMAT.parse(parts[4]);
                    double totalRent = Double.parseDouble(parts[5]);
                    String status = parts[6];
                    
                    RentalRecord record = new RentalRecord();
                    record.setId(id);
                    
                    // 通过repository查找车辆和客户对象
                    if (vehicleRepository != null) {
                        Optional<Vehicle> vehicleOpt = vehicleRepository.findVehicleById(vehicleId);
                        vehicleOpt.ifPresent(record::setVehicle);
                    }
                    
                    if (customerRepository != null) {
                        Optional<Customer> customerOpt = customerRepository.findCustomerById(customerId);
                        customerOpt.ifPresent(record::setCustomer);
                    }
                    
                    record.setRentalDate(rentalDate);
                    record.setReturnDate(returnDate);
                    record.setTotalRent(totalRent);
                    
                    // 设置是否已归还状态
                    record.setReturned("已归还".equals(status));
                    
                    rentalRecords.add(record);
                }
            } catch (NumberFormatException | ParseException e) {
                System.err.println("解析租赁记录数据失败: " + line);
            }
        }
    }

    /**
     * 将租赁记录数据保存到文件
     */
    @Override
    public void saveRentalRecords() {
        List<String> lines = new ArrayList<>();
        
        // 将所有租赁记录转换为字符串格式
        for (RentalRecord record : rentalRecords) {
            int vehicleId = record.getVehicle() != null ? record.getVehicle().getId() : 0;
            int customerId = record.getCustomer() != null ? record.getCustomer().getId() : 0;
            String rentalDateStr = DATE_FORMAT.format(record.getRentalDate());
            String returnDateStr = record.getReturnDate() != null ? DATE_FORMAT.format(record.getReturnDate()) : "null";
            String status = record.isReturned() ? "已归还" : "未归还";
            
            String line = String.format("%d,%d,%d,%s,%s,%.2f,%s",
                    record.getId(),
                    vehicleId,
                    customerId,
                    rentalDateStr,
                    returnDateStr,
                    record.getTotalRent(),
                    status);
            lines.add(line);
        }
        
        // 写入文件
        FileUtil.writeLines(Constants.RENTAL_FILE_PATH, lines);
    }
}