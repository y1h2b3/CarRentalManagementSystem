package com.carrental.controller;

import com.carrental.model.User;
import com.carrental.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

/**
 * 用户控制器，处理用户登录和用户管理界面
 */
public class UserController {
    private final UserService userService;
    private Scanner scanner;
    private User currentUser; // 当前登录用户
    
    /**
     * 构造方法，注入用户服务依赖
     * @param userService 用户服务
     */
    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * 显示登录界面并处理登录逻辑
     * @return 登录成功返回true，否则返回false
     */
    public boolean showLoginScreen() {
        System.out.println("========================================");
        System.out.println("              汽车租赁管理系统             ");
        System.out.println("========================================");
        System.out.println("             用户登录");
        System.out.println("========================================");
        
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        
        // 调用服务层进行登录验证
        Optional<User> userOpt = userService.login(username, password);
        
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("\n登录成功！欢迎回来，" + username);
            System.out.println("角色: " + currentUser.getRole());
            return true;
        } else {
            System.out.println("\n登录失败！用户名或密码错误。");
            return false;
        }
    }
    
    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 用户登出
     */
    public void logout() {
        // 检查用户是否已经登出
        if (currentUser == null) {
            System.out.println("您尚未登录系统，无需登出。");
            return;
        }
        
        // 记录登出的用户名
        String username = currentUser.getUsername();
        
        // 执行登出操作
        currentUser = null;
        
        // 提供友好的用户反馈
        System.out.println("尊敬的用户 " + username + "，您已成功登出系统。");
        System.out.println("感谢您的使用，期待您再次登录！");
    }
//
    /**
     * 显示用户管理菜单（管理员功能）
     */
    public void showUserManagementMenu() {
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            System.out.println("无权限访问用户管理功能。");
            return;
        }
        
        boolean exit = false;
        while (!exit) {
            System.out.println("\n========================================");
            System.out.println("              用户管理");
            System.out.println("========================================");
            System.out.println("1. 添加用户");
            System.out.println("2. 更新用户信息");
            System.out.println("3. 删除用户");
            System.out.println("4. 查看所有用户");
            System.out.println("0. 返回主菜单");
            System.out.println("========================================");
            
            System.out.print("请选择操作 (1-4): ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    addUser();
                    break;
                case "2":
                    updateUser();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    displayAllUser();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }
    
    /**
     * 添加用户
     */
    private void addUser() {
        System.out.println("\n------ 添加用户 ------");
        
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim(); //.trim() 去除字符串首尾的空白字符（包括空格、制表符 \t、换行符 \n 等不可见的空白符号），中间的空白字符会保留。
        
        // 检查用户名是否已存在
        if (userService.findByUsername(username).isPresent()) {
            System.out.println("用户名已存在！");
            return;
        }
        
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("请输入角色 (ADMIN/USER): ");
        String role = scanner.nextLine().trim().toUpperCase();
        
        // 验证角色
        if (!"ADMIN".equals(role) && !"USER".equals(role)) {
            System.out.println("无效的角色！请输入 ADMIN 或 USER。");
            return;
        }
        
        // 创建用户对象
        User newUser = new User(username, password, role);
        
        // 添加用户
        if (userService.addUser(newUser)) {
            System.out.println("用户添加成功！");
        } else {
            System.out.println("用户添加失败！");
        }
    }
    
    /**
     * 更新用户信息
     */
    private void updateUser() {
        System.out.println("\n------ 更新用户信息 ------");
        
        System.out.print("请输入要更新的用户名: ");
        String username = scanner.nextLine().trim();
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            System.out.println("用户不存在！");
            return;
        }
        
        User user = userOpt.get();
        System.out.println("当前用户信息: " + user);
        
        System.out.print("请输入新密码 (留空不修改): ");
        String newPassword = scanner.nextLine().trim();
        if (!newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }
        
        System.out.print("请输入新角色 (ADMIN/USER，留空不修改): ");
        String newRole = scanner.nextLine().trim().toUpperCase();
        if (!newRole.isEmpty()) {
            if ("ADMIN".equals(newRole) || "USER".equals(newRole)) {
                user.setRole(newRole);
            } else {
                System.out.println("无效的角色！保持原有角色。");
            }
        }
        
        // 更新用户
        if (userService.updateUser(user)) {
            System.out.println("用户信息更新成功！");
        } else {
            System.out.println("用户信息更新失败！");
        }
    }
    
    /**
     * 删除用户
     */
    private void deleteUser() {
        System.out.println("\n------ 删除用户 ------");
        
        System.out.print("请输入要删除的用户名: ");
        String username = scanner.nextLine().trim();
        
        // 不能删除当前登录的管理员账户
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            System.out.println("不能删除当前登录的账户！");
            return;
        }
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            System.out.println("用户不存在！");
            return;
        }
        
        // 确认删除
        System.out.print("确定要删除用户 "+username+" 吗？(y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if ("y".equals(confirm)) {
            if (userService.deleteUser(currentUser, username)) {
                System.out.println("用户删除成功！");
            } else {
                System.out.println("用户删除失败！");
            }
        } else {
            System.out.println("已取消删除操作。");
        }
    }

    public void displayAllUser() {
        System.out.printf("%-15s %-15s \n",
                "用户名", "角色");
        System.out.println("-----------------------------------------------------------------------------");
        List<User> allUsers = userService.findAllUsers();
        if (allUsers == null) {
            System.out.println("当前无用户！");
        } else {
            for (User user : allUsers) {
                System.out.printf("%-15s %-15s\n",
                        user.getUsername(),
                        user.getRole());
            }
        }

    }
}