
import com.carrental.controller.*;
import com.carrental.repository.*;
import com.carrental.repository.Impl.*;
import com.carrental.service.*;
import com.carrental.service.Impl.*;

/**
 * 汽车租赁管理系统入口类
 */
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
