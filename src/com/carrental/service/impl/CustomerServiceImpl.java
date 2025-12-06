package com.carrental.service.impl;

import com.carrental.model.Customer;
import com.carrental.repository.CustomerRepository;
import com.carrental.service.CustomerService;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务实现类
 */
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    // 构造函数注入依赖
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 添加客户
     * @param customer 客户对象
     * @return 添加是否成功
     */
    @Override
    public boolean addCustomer(Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    /**
     * 删除客户
     * @param id 客户ID
     * @return 删除是否成功
     */
    @Override
    public boolean deleteCustomer(int id) {
        return customerRepository.deleteCustomer(id);
    }

    /**
     * 更新客户信息
     * @param customer 客户对象
     * @return 更新是否成功
     */
    @Override
    public boolean updateCustomer(Customer customer) {
        return customerRepository.updateCustomer(customer);
    }

    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<Customer> findCustomerById(int id) {
        return customerRepository.findCustomerById(id);
    }

    /**
     * 查询所有客户
     * @return 所有客户列表
     */
    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    /**
     * 根据客户类型查询客户
     * @param type 客户类型
     * @return 对应类型的客户列表
     */
    @Override
    public List<Customer> findCustomersByType(String type) {
        return customerRepository.findCustomersByType(type);
    }
}