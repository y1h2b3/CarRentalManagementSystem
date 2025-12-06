# 汽车租赁管理系统项目报告

## 1. 项目概述

汽车租赁管理系统是一个用于管理汽车租赁业务的综合信息系统，主要功能包括用户认证、汽车信息管理、客户信息管理和日常业务管理（租车、还车）。系统采用分层架构设计，具有良好的可扩展性和维护性。

### 1.1 系统功能

- **用户认证**：支持系统用户登录验证、角色管理和权限控制，确保系统安全性
- **用户管理**：支持用户增删改查，区分管理员和普通用户权限
- **汽车信息管理**：支持客车、轿车、面包车的增删改查操作
- **客户信息管理**：支持普通客户、VIP客户和企业客户的增删改查操作
- **日常业务管理**：支持租车和还车功能，自动计算租金（根据客户类型应用不同折扣）
- **租赁记录管理**：记录所有租赁交易信息，支持查询未归还记录
- **退出登录**：支持用户安全退出，返回登录页面

## 2. 项目结构

系统采用标准的MVC（Model-View-Controller）架构，代码结构清晰，职责分明。项目包结构如下：
![img.png](img.png)
```
src/
├── Main.java                    # 系统入口类
└── com/carrental/
    ├── controller/              # 控制器层
    │   ├── MainController.java  # 主控制器
    │   ├── VehicleController.java  # 汽车信息管理控制器
    │   ├── CustomerController.java  # 客户信息管理控制器
    │   ├── RentalController.java  # 租赁业务管理控制器
    │   └── UserController.java  # 用户管理控制器
    ├── model/                   # 数据模型层
    │   ├── Vehicle.java         # 车辆基类
    │   ├── Bus.java             # 客车类
    │   ├── Car.java             # 轿车类
    │   ├── Van.java             # 面包车类
    │   ├── Customer.java        # 客户类
    │   ├── RentalRecord.java    # 租赁记录类
    │   └── User.java            # 用户类
    ├── repository/              # 数据访问层
    │   ├── VehicleRepository.java  # 车辆数据访问接口
    │   ├── CustomerRepository.java  # 客户数据访问接口
    │   ├── RentalRecordRepository.java  # 租赁记录数据访问接口
    │   ├── UserRepository.java  # 用户数据访问接口
    │   └── impl/                # 数据访问实现包
    │       ├── VehicleRepositoryFileImpl.java  # 车辆数据访问实现
    │       ├── CustomerRepositoryFileImpl.java  # 客户数据访问实现
    │       ├── RentalRecordRepositoryFileImpl.java  # 租赁记录数据访问实现
    │       └── UserRepositoryImpl.java  # 用户数据访问实现
    ├── service/                 # 业务逻辑层
    │   ├── VehicleService.java  # 车辆服务接口
    │   ├── CustomerService.java  # 客户服务接口
    │   ├── RentalService.java  # 租赁服务接口
    │   ├── UserService.java  # 用户服务接口
    │   └── impl/                # 业务逻辑实现包
    │       ├── VehicleServiceImpl.java  # 车辆服务实现
    │       ├── CustomerServiceImpl.java  # 客户服务实现
    │       ├── RentalServiceImpl.java  # 租赁服务实现
    │       └── UserServiceImpl.java  # 用户服务实现
    └── util/                    # 工具类
        ├── Constants.java       # 常量定义
        ├── InputUtil.java       # 输入工具类
        └── FileUtil.java        # 文件操作工具类
```

## 3. 系统架构

系统采用分层架构设计，各层之间通过接口进行通信，实现了解耦和模块化：

### 3.1 分层架构

1. **表示层**：通过控制台界面与用户交互，接收用户输入并显示系统输出
2. **控制器层（Controller）**：处理用户请求，调用相应的服务层方法，并将结果返回给表示层
3. **业务逻辑层（Service）**：实现核心业务逻辑，处理业务规则和数据验证
4. **数据访问层（Repository）**：负责数据的持久化和访问，提供统一的数据操作接口
5. **数据模型层（Model）**：定义系统的核心数据结构，包括实体类和值对象

### 3.2 系统启动流程

系统启动时，按照以下流程初始化各层组件：
1. 创建数据访问层（Repository）实现类
2. 设置Repository之间的依赖关系（如RentalRecordRepository需要访问Vehicle和Customer数据）
3. 创建业务逻辑层（Service）实现类，注入对应的Repository依赖
4. 创建控制器层（Controller）实现类，注入对应的Service依赖
5. 创建主控制器，注入所有功能控制器
6. 启动用户认证流程，验证通过后进入主菜单
7. 主菜单提供退出登录选项，支持用户安全退出并返回登录页面

### 3.2 依赖关系

- 控制器层依赖于业务逻辑层
- 业务逻辑层依赖于数据访问层
- 数据访问层依赖于数据模型层

## 4. 核心功能实现

### 4.1 用户认证管理

用户认证模块实现了系统的安全访问控制：

- **用户登录**：支持用户名和密码验证，最多允许3次尝试
- **角色管理**：支持不同用户角色的定义和权限控制（ADMIN/USER）
- **权限控制**：
  - 只有ADMIN角色可以删除用户
  - USER角色用户不可以删除用户
  - ADMIN用户不可以被删除
- **安全机制**：防止未授权访问系统功能
- **退出登录**：支持用户安全退出，退出后返回登录页面
- **登录循环**：用户退出登录后，系统返回登录页面，允许重新登录

### 4.2 汽车信息管理

汽车信息管理模块实现了对不同类型车辆的增删改查功能：

- **添加车辆**：支持添加客车、轿车和面包车，录入品牌、车型、日租金等信息
- **修改车辆**：支持修改车辆的基本信息
- **删除车辆**：支持删除未出租的车辆
- **查询车辆**：支持按类型查询和查看所有车辆
- **数据持久化**：车辆信息自动保存到文件中

### 4.3 客户信息管理

客户信息管理模块实现了对不同类型客户的增删改查功能：

- **添加客户**：支持添加普通客户、VIP客户和企业客户，录入姓名、电话等信息
- **修改客户**：支持修改客户的基本信息和客户类型
- **删除客户**：支持删除客户信息
- **查询客户**：支持查看所有客户信息
- **数据持久化**：客户信息自动保存到文件中

### 4.4 日常业务管理

日常业务管理模块实现了租车和还车功能：

- **租车功能**：用户选择可租赁车辆和客户，输入租赁天数，系统自动计算租金并生成租赁记录
- **还车功能**：用户输入租赁记录ID，系统标记车辆为可出租状态并更新租赁记录
- **租赁记录查询**：支持查看所有租赁记录、未归还的租赁记录、按车辆ID查询和按客户ID查询
- **数据持久化**：租赁记录自动保存到文件中

### 4.5 租金计算

系统根据车辆的日租金、客户类型和租赁天数计算总租金：

- 普通客户：无折扣（100%）
- VIP客户：95折优惠
- 企业客户：90折优惠
- 租金 = 车辆日租金 × 租赁天数 × 客户类型折扣率

## 5. 技术细节

### 5.1 数据模型设计

系统使用面向对象的方式设计数据模型：

- **Vehicle**：车辆基类，包含所有车辆共有的属性（ID、类型、品牌、车型、日租金、是否出租）
- **Bus/Car/Van**：车辆子类，继承自Vehicle，可扩展特定类型车辆的属性
- **Customer**：客户类，包含客户ID、姓名、电话、客户类型等属性
- **RentalRecord**：租赁记录类，记录车辆、客户、租赁日期、归还日期、租赁天数、总租金、是否已归还等信息
- **User**：用户类，包含用户名、密码、角色等属性，用于系统登录验证

### 5.2 接口设计

系统在业务逻辑层和数据访问层都使用了接口，实现了面向接口编程：

- **Service接口**：定义业务逻辑方法，如VehicleService、CustomerService、RentalService、UserService
- **Repository接口**：定义数据访问方法，如VehicleRepository、CustomerRepository、RentalRecordRepository、UserRepository

### 5.3 依赖注入

系统使用构造函数注入的方式实现依赖注入，提高了代码的可测试性和灵活性：

```java
public class VehicleServiceImpl implements VehicleService {
    private VehicleRepository vehicleRepository;

    // 构造函数注入依赖
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    // ...
}
```

### 5.4 工具类

- **Constants**：定义系统常量，如客户类型、车辆类型、折扣率、文件路径等
- **InputUtil**：提供统一的输入处理方法，包含错误处理
- **FileUtil**：提供文件读写操作的工具方法，支持数据持久化

## 6. 代码示例

### 6.1 系统初始化与依赖注入实现

```java
public class Main {
    public static void main(String[] args) {
        // 初始化数据访问层
        VehicleRepository vehicleRepository = new VehicleRepositoryFileImpl();
        CustomerRepository customerRepository = new CustomerRepositoryFileImpl();
        RentalRecordRepositoryFileImpl rentalRecordRepository = new RentalRecordRepositoryFileImpl();
        
        // 设置Repository之间的依赖关系
        rentalRecordRepository.setRepository(vehicleRepository,customerRepository);

        UserRepository userRepository = new UserRepositoryImpl();

        // 初始化Service层
        VehicleService vehicleService = new VehicleServiceImpl(vehicleRepository);
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        RentalService rentalService = new RentalServiceImpl(vehicleService, customerService, rentalRecordRepository);
        UserService userService = new UserServiceImpl(userRepository);

        // 初始化Controller层
        VehicleController vehicleController = new VehicleController(vehicleService);
        CustomerController customerController = new CustomerController(customerService);
        RentalController rentalController = new RentalController(rentalService, vehicleService, customerService, vehicleController, customerController);
        UserController userController = new UserController(userService);

        // 初始化主控制器，传入用户控制器
        MainController mainController = new MainController(vehicleController, customerController, rentalController, userController);
        
        // 用户登录验证和系统主循环
        while (true) {
            boolean loggedIn = false;
            int maxAttempts = 3;
            int attempts = 0;
            
            // 登录循环
            while (!loggedIn && attempts < maxAttempts) {
                loggedIn = userController.showLoginScreen();
                attempts++;
                
                if (!loggedIn && attempts < maxAttempts) {
                    System.out.println("剩余尝试次数: " + (maxAttempts - attempts));
                    try {
                        Thread.sleep(1000); // 等待1秒后重试
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
            if (loggedIn) {
                // 登录成功，启动系统主菜单
                mainController.showMainMenu();
            } else {
                // 登录失败次数过多
                System.out.println("登录失败次数过多，系统即将退出。");
                break;
            }
        }
    }
}
```

### 6.2 租车功能实现

```java
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
```

### 6.3 租金计算实现

```java
/**
 * 计算租金
 * @param vehicleId 车辆ID
 * @param customerId 客户ID
 * @param rentalDays 租赁天数
 * @return 计算后的总租金
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
    
    // 获取车辆日租金
    double dailyRent = vehicle.getDailyRent();
    
    // 根据客户类型获取折扣率
    double discount = getDiscountByCustomerType(customer.getCustomerType());
    
    // 计算总租金
    return dailyRent * rentalDays * discount;
}

/**
 * 根据客户类型获取折扣率
 * @param customerType 客户类型
 * @return 折扣率
 */
private double getDiscountByCustomerType(String customerType) {
    if (Constants.CUSTOMER_TYPE_VIP.equals(customerType)) {
        return Constants.DISCOUNT_VIP;
    } else if (Constants.CUSTOMER_TYPE_ENTERPRISE.equals(customerType)) {
        return Constants.DISCOUNT_ENTERPRISE;
    } else {
        return Constants.DISCOUNT_REGULAR;
    }
}
```

## 7. 测试与验证

系统提供了完整的测试功能，包括：

- 用户认证系统，支持安全登录
- 初始化示例数据（车辆、客户和用户）
- 控制台交互界面，方便测试所有功能
- 错误处理机制，确保系统稳定性
- 文件持久化存储，保证数据不会丢失

### 7.1 测试流程

1. 启动系统，进行用户登录验证
2. 测试汽车信息管理功能（增删改查）
3. 测试客户信息管理功能（增删改查）
4. 测试日常业务管理功能（租车、还车）
5. 测试租赁记录查询功能

## 8. 总结

汽车租赁管理系统采用分层架构设计，实现了用户认证、汽车信息管理、客户信息管理和日常业务管理四大核心功能。系统具有以下特点：

- **良好的架构设计**：采用分层架构，实现了解耦和模块化
- **面向接口编程**：使用接口定义各层之间的通信规范
- **依赖注入**：提高了代码的可测试性和灵活性
- **完善的错误处理**：确保系统的稳定性和用户体验
- **文件持久化**：使用文件存储数据，保证数据不会丢失
- **用户认证系统**：提供安全的登录验证机制
- **易扩展的设计**：方便添加新功能和修改现有功能

该系统满足了汽车租赁业务的基本需求，提供了友好的用户界面和完整的功能实现。通过文件持久化存储和用户认证系统，进一步增强了系统的实用性和安全性。