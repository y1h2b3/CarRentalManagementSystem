package com.carrental.controller;

import com.carrental.util.InputUtil;

/**
 * 主控制器，协调各个子控制器并提供系统主菜单
 */
public class MainController {
    private VehicleController vehicleController;
    private CustomerController customerController;
    private RentalController rentalController;
    private UserController userController;



    // 构造函数，包含用户控制器
    public MainController(VehicleController vehicleController, CustomerController customerController, RentalController rentalController, UserController userController) {
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.rentalController = rentalController;
        this.userController = userController;
    }

    /**
     * 显示系统主菜单
     */
    public void showMainMenu() {
        while (true) {
            System.out.println("===== 汽车租赁管理系统 =====");
            System.out.println("1. 汽车信息管理");
            System.out.println("2. 客户信息管理");
            System.out.println("3. 日常业务管理");
            if (userController != null) {
                System.out.println("4. 用户管理");
            }
            System.out.println("5. 退出登录");
            System.out.println("0. 退出系统");

            int choice = InputUtil.getInt("请选择功能模块: ");

            switch (choice) {
                case 1:
                    vehicleController.showMenu();
                    break;
                case 2:
                    customerController.showMenu();
                    break;
                case 3:
                    rentalController.showMenu();
                    break;
                case 4:
                    if (userController != null) {
                        userController.showUserManagementMenu();
                    } else {
                        System.out.println("用户管理功能未启用！");
                    }
                    break;
                case 5:
                    // 退出登录
                    if (userController != null) {
                        userController.logout();
                    }
                    return; // 返回Main类，重新开始登录流程
                case 0:
                    System.out.println("感谢使用汽车租赁管理系统，再见！");
                    InputUtil.close();
                    System.exit(0);
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
}