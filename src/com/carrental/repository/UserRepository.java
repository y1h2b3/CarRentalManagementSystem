package com.carrental.repository;

import com.carrental.model.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口，定义用户相关的数据操作方法
 */
public interface UserRepository {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 如果找到用户返回User对象的Optional，否则返回空Optional：解决 “空指针”
     */
    Optional<User> findByUsername(String username);

    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加成功返回true，否则返回false
     */
    boolean addUser(User user);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true，否则返回false
     */
    boolean updateUser(User user);
    
    /**
     * 删除用户
     * @param username 用户名
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteUser(String username);

    /**
     * 查询所有用户
     * @return 所有用户列表
     */
    List<User> findAllUsers();
   
    /**
     * 从文件加载用户数据
     */
    void loadUsers();
    
    /**
     * 将用户数据保存到文件
     */
    void saveUsers();
}