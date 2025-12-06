package com.carrental.service;

import com.carrental.model.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口，定义用户相关的业务逻辑
 */
public interface UserService {
    
    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 验证成功返回用户对象，否则返回空Optional
     */
    Optional<User> login(String username, String password);
    
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
     * @param currentUser 当前登录用户
     * @param username 要删除的用户名
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteUser(User currentUser, String username);

    /**
     * 查询所有用户
     * @return 所有用户列表
     */
    List<User> findAllUsers();

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象的Optional
     */
    Optional<User> findByUsername(String username);
}