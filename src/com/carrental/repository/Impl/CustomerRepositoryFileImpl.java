package com.carrental.repository.impl;

import com.carrental.model.Customer;
import com.carrental.repository.CustomerRepository;
import com.carrental.util.Constants;
import com.carrental.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户数据访问实现类，使用文件存储客户数据
 */
public class CustomerRepositoryFileImpl implements CustomerRepository {
    private List<Customer> customers; // 客户列表
    
    /**
     * 构造方法，初始化客户列表并加载文件中的数据
     */
    public CustomerRepositoryFileImpl() {
        this.customers = new ArrayList<>();
        loadCustomers();
        
        // 如果没有客户数据，添加一些示例客户
        if (customers.isEmpty()) {
            addSampleCustomers();
        }
    }

    /**
     * 添加示例客户数据
     */
    private void addSampleCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("张三");
        customer1.setCustomerType(Constants.CUSTOMER_TYPE_REGULAR);
        customer1.setPhone("13812345678");
        addCustomer(customer1);
        
        Customer customer2 = new Customer();
        customer2.setName("李四");
        customer2.setCustomerType(Constants.CUSTOMER_TYPE_VIP);
        customer2.setPhone("13987654321");
        addCustomer(customer2);
        
        Customer customer3 = new Customer();
        customer3.setName("王五");
        customer3.setCustomerType(Constants.CUSTOMER_TYPE_REGULAR);
        customer3.setPhone("13712345678");
        addCustomer(customer3);
        
        Customer customer4 = new Customer();
        customer4.setName("北京科技有限公司");
        customer4.setCustomerType(Constants.CUSTOMER_TYPE_ENTERPRISE);
        customer4.setPhone("010-12345678");
        addCustomer(customer4);
        
        Customer customer5 = new Customer();
        customer5.setName("上海贸易有限公司");
        customer5.setCustomerType(Constants.CUSTOMER_TYPE_ENTERPRISE);
        customer5.setPhone("021-87654321");
        addCustomer(customer5);
    }

    /**
     * 添加客户
     * @param customer 客户对象
     * @return 添加是否成功
     */

    @Override
    public boolean addCustomer(Customer customer) {
        if (customer != null) {
            // 设置客户ID
            customer.setId(getNextCustomerId());
            customers.add(customer);
            saveCustomers(); // 自动保存到文件
            return true;
        }
        return false;
    }

    /**
     * 删除客户
     * @param id 客户ID
     * @return 删除是否成功
     */
    @Override
    public boolean deleteCustomer(int id) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                customers.remove(i);
                saveCustomers(); // 自动保存到文件
                return true;
            }
        }
        return false;
    }

    /**
     * 更新客户信息
     * @param customer 客户对象
     * @return 更新是否成功
     */
    @Override
    public boolean updateCustomer(Customer customer) {
        if (customer == null) return false;

        // 检查客户是否存在
        Optional<Customer> optionalCustomer = findCustomerById(customer.getId());
        if (optionalCustomer.isPresent()) {
            // 客户对象在Controller层已经被修改，直接保存到文件
            saveCustomers();
            return true;
        }
        return false;
    }

    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户对象，如果不存在则返回Optional.empty()
     */
    @Override
    public Optional<Customer> findCustomerById(int id) {
        return customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst();
    }

    /**
     * 查询所有客户
     * @return 所有客户列表
     */
    @Override
    public List<Customer> findAllCustomers() {
        return new ArrayList<>(customers);
    }

    /**
     * 根据客户类型查询客户
     * @param type 客户类型
     * @return 对应类型的客户列表
     */
    @Override
    public List<Customer> findCustomersByType(String type) {
        return customers.stream()
                .filter(customer -> customer.getCustomerType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * 获取下一个可用的客户ID
     * @return 下一个可用的客户ID
     */
    @Override
    public int getNextCustomerId() {
        // 如果没有客户，返回1
        if (customers.isEmpty()) {
            return 1;
        }
        
        // 找到最大的ID，然后加1
        int maxId = customers.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0);
        
        return maxId + 1;
    }

    /**
     * 从文件加载客户数据
     */
    @Override
    public void loadCustomers() {
        List<String> lines = FileUtil.readLines(Constants.CUSTOMER_FILE_PATH);
        customers.clear();
        
        for (String line : lines) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                continue;
            }
            
            try {
                // 解析客户数据
                // 格式：ID,姓名,类型,电话
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String type = parts[2];
                    String phone = parts[3];
                    
                    Customer customer = new Customer();
                    customer.setId(id);
                    customer.setName(name);
                    customer.setCustomerType(type);
                    customer.setPhone(phone);
                    customers.add(customer);
                }
            } catch (NumberFormatException e) {
                System.err.println("解析客户数据失败: " + line);
            }
        }
    }

    /**
     * 将客户数据保存到文件
     */
    @Override
    public void saveCustomers() {
        List<String> lines = new ArrayList<>();
        
        // 将所有客户转换为字符串格式
        for (Customer customer : customers) {
            String line = String.format("%d,%s,%s,%s",
                    customer.getId(),
                    customer.getName(),
                    customer.getCustomerType(),
                    customer.getPhone());
            lines.add(line);
        }
        
        // 写入文件
        FileUtil.writeLines(Constants.CUSTOMER_FILE_PATH, lines);
    }
}