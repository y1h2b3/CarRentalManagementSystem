package com.carrental.util;

/**
 * 常量类
 */
public class Constants {
    // 客户类型
    public static final String CUSTOMER_TYPE_REGULAR = "普通";
    public static final String CUSTOMER_TYPE_VIP = "VIP";
    public static final String CUSTOMER_TYPE_ENTERPRISE = "企业";

    // 车辆类型
    public static final String VEHICLE_TYPE_BUS = "客车";
    public static final String VEHICLE_TYPE_CAR = "轿车";
    public static final String VEHICLE_TYPE_VAN = "面包车";

    // 车辆数据文件路径
    public static final String VEHICLE_FILE_PATH = "./data/vehicles.txt";
    // 用户数据文件路径
    public static final String USER_FILE_PATH = "./data/users.txt";
    // 租赁记录文件路径
    public static final String RENTAL_FILE_PATH = "./data/rental_records.txt";
    // 客户数据文件路径
    public static final String CUSTOMER_FILE_PATH = "./data/customers.txt";

    // 折扣率
    public static final double DISCOUNT_REGULAR = 1.0;    // 普通客户无折扣
    public static final double DISCOUNT_VIP = 0.95;       // VIP客户95折
    public static final double DISCOUNT_ENTERPRISE = 0.9; // 企业客户9折

}