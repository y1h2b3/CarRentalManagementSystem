package com.carrental.service;

import com.carrental.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务接口，定义客户相关的业务逻辑
 */
public interface CustomerService {
    /**
     * 添加客户
     * @param customer 客户对象
     * @return 添加是否成功
     */
    boolean addCustomer(Customer customer);

    /**
     * 删除客户
     * @param id 客户ID
     * @return 删除是否成功
     */
    boolean deleteCustomer(int id);

    /**
     * 更新客户信息
     * @param customer 客户对象
     * @return 更新是否成功
     */
    boolean updateCustomer(Customer customer);

    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户对象，如果不存在则返回Optional.empty()
     */
    Optional<Customer> findCustomerById(int id);

    /**
     * 查询所有客户
     * @return 所有客户列表
     */
    List<Customer> findAllCustomers();

    /**
     * 根据客户类型查询客户
     * @param type 客户类型
     * @return 对应类型的客户列表
     */
    List<Customer> findCustomersByType(String type);
}