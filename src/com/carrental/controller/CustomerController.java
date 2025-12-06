package com.carrental.controller;

import com.carrental.model.Customer;
import com.carrental.service.CustomerService;
import com.carrental.util.Constants;
import com.carrental.util.InputUtil;

import java.util.List;
import java.util.Optional;

/**
 * 客户信息管理控制器，处理客户相关的用户交互
 */
public class CustomerController {
    private CustomerService customerService;

    // 构造函数注入依赖
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 显示客户信息管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n===== 客户信息管理 =====");
            System.out.println("1. 添加客户");
            System.out.println("2. 修改客户");
            System.out.println("3. 删除客户");
            System.out.println("4. 查看客户列表");
            System.out.println("0. 返回上一级");

            int choice = InputUtil.getInt("请选择操作: ");

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    updateCustomer();
                    break;
                case 3:
                    deleteCustomer();
                    break;
                case 4:
                    displayAllCustomers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    /**
     * 添加客户
     */
    private void addCustomer() {
        System.out.println("\n===== 添加客户 =====");
        String name = InputUtil.getNonEmptyString("请输入客户姓名: ");
        String phone = InputUtil.getNonEmptyString("请输入客户电话: ");
        
        System.out.println("\n客户类型:");
        System.out.println("1. 普通客户");
        System.out.println("2. VIP客户");
        System.out.println("3. 企业客户");
        int typeChoice = InputUtil.getInt("请选择客户类型: ");
        
        String customerType;
        switch (typeChoice) {
            case 1:
                customerType = Constants.CUSTOMER_TYPE_REGULAR;
                break;
            case 2:
                customerType = Constants.CUSTOMER_TYPE_VIP;
                break;
            case 3:
                customerType = Constants.CUSTOMER_TYPE_ENTERPRISE;
                break;
            default:
                System.out.println("无效的客户类型，默认使用普通客户");
                customerType = Constants.CUSTOMER_TYPE_REGULAR;
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setCustomerType(customerType);

        if (customerService.addCustomer(customer)) {
            System.out.println("添加客户成功！");
        } else {
            System.out.println("添加客户失败！");
        }
    }

    /**
     * 修改客户
     */
    private void updateCustomer() {
        System.out.println("\n===== 修改客户 =====");
        displayAllCustomers();

        int id = InputUtil.getInt("请输入要修改的客户ID: ");
        Optional<Customer> optionalCustomer = customerService.findCustomerById(id);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            System.out.println("当前客户信息: " + customer);

            String name = InputUtil.getString("请输入新姓名 (回车保持不变): ");
            if (!name.trim().isEmpty()) {
                customer.setName(name);
            }

            String phone = InputUtil.getString("请输入新电话 (回车保持不变): ");
            if (!phone.trim().isEmpty()) {
                customer.setPhone(phone);
            }

            String typeChoiceStr = InputUtil.getString("请选择新客户类型 (1-普通, 2-VIP, 3-企业, 回车保持不变): ");
            if (!typeChoiceStr.trim().isEmpty()) {
                try {
                    int typeChoice = Integer.parseInt(typeChoiceStr);
                    switch (typeChoice) {
                        case 1:
                            customer.setCustomerType(Constants.CUSTOMER_TYPE_REGULAR);
                            break;
                        case 2:
                            customer.setCustomerType(Constants.CUSTOMER_TYPE_VIP);
                            break;
                        case 3:
                            customer.setCustomerType(Constants.CUSTOMER_TYPE_ENTERPRISE);
                            break;
                        default:
                            System.out.println("无效的客户类型，保持原有值！");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("输入错误，保持原有客户类型！");
                }
            }

            if (customerService.updateCustomer(customer)) {
                System.out.println("修改客户成功！");
            } else {
                System.out.println("修改客户失败！");
            }
        } else {
            System.out.println("未找到ID为" + id + "的客户！");
        }
    }

    /**
     * 删除客户
     */
    private void deleteCustomer() {
        System.out.println("\n===== 删除客户 =====");
        displayAllCustomers();

        int id = InputUtil.getInt("请输入要删除的客户ID: ");
        Optional<Customer> optionalCustomer = customerService.findCustomerById(id);

        if (optionalCustomer.isPresent()) {
            if (customerService.deleteCustomer(id)) {
                System.out.println("删除客户成功！");
            } else {
                System.out.println("删除客户失败！");
            }
        } else {
            System.out.println("未找到ID为" + id + "的客户！");
        }
    }

    /**
     * 显示所有客户
     */
    public void displayAllCustomers() {
        System.out.println("\n===== 客户列表 =====");
        List<Customer> customers = customerService.findAllCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("没有客户记录！");
            return;
        }

        System.out.printf("%-5s %-10s %-15s %-15s\n", 
                "ID", "姓名", "电话", "客户类型");
        System.out.println("------------------------------------------------");
        for (Customer customer : customers) {
            System.out.printf("%-5d %-10s %-15s %-15s\n",
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getCustomerType());
        }
    }
}