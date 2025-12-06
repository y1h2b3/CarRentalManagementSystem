package com.carrental.service.impl;

import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类，实现用户相关的业务逻辑
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    
    /**
     * 构造方法，注入用户仓库依赖
     * @param userRepository 用户仓库
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 验证成功返回用户对象，否则返回空Optional
     */
    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        // 如果找到用户且密码正确，返回用户对象
        if (userOpt.isPresent() && userOpt.get().validatePassword(password)) {
            return userOpt;
        }
        
        // 登录失败返回空
        return Optional.empty();
    }
    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加成功返回true，否则返回false
     */
    @Override
    public boolean addUser(User user) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        
        // 添加用户
        return userRepository.addUser(user);
    }
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true，否则返回false
     */
    @Override
    public boolean updateUser(User user) {
        // 检查用户是否存在
        if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        
        // 更新用户信息
        return userRepository.updateUser(user);
    }
    /**
     * 删除用户
     * @param currentUser 当前登录用户
     * @param username 要删除的用户名
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean deleteUser(User currentUser, String username) {
        // 检查用户是否存在
        if (!userRepository.findByUsername(username).isPresent()) {
            return false;
        }
        
        // 删除用户
        return userRepository.deleteUser(currentUser, username);
    }
    /**
     * 查询所有用户
     * @return 所有用户列表
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象的Optional
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}