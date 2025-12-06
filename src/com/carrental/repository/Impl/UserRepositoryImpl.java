package com.carrental.repository.impl;

import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import com.carrental.util.Constants;
import com.carrental.util.FileUtil;


import java.util.*;

/**
 * 用户数据访问实现类，使用文件存储用户数据
 */
public class UserRepositoryImpl implements UserRepository {
    private Map<String, User> userMap; // 用户名到用户对象的映射，用于快速查找
    
    /**
     * 构造方法，初始化用户数据映射并加载文件中的数据
     */
    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        loadUsers();
        
        // 如果没有用户数据，添加默认管理员账户
        if (userMap.isEmpty()) {
            addDefaultAdmin();
        }
    }
    
    /**
     * 添加默认管理员账户
     */
    private void addDefaultAdmin() {
        User admin = new User("admin", "admin123", "ADMIN");
        addUser(admin);
        saveUsers(); // 保存到文件
    }
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 如果找到用户返回User对象的Optional，否则返回空Optional：解决 “空指针”
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }
    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加成功返回true，否则返回false
     */
    @Override
    public boolean addUser(User user) {
        if (userMap.containsKey(user.getUsername())) {
            return false; // 用户名已存在
        }
        userMap.put(user.getUsername(), user);
        saveUsers(); // 自动保存到文件
        return true;
    }
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true，否则返回false
     */
    @Override
    public boolean updateUser(User user) {
        if (!userMap.containsKey(user.getUsername())) {
            return false; // 用户不存在
        }
        userMap.put(user.getUsername(), user);
        saveUsers(); // 自动保存到文件
        return true;
    }
    /**
     * 删除用户
     * @param currentUser 当前登录用户
     * @param username 要删除的用户名
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean deleteUser(User currentUser, String username) {
        // 检查要删除的用户是否存在
        if (!userMap.containsKey(username)) {
            return false; // 用户不存在
        }
        
        // 检查当前用户是否是ADMIN角色（只有ADMIN可以删除用户）
        if (!"ADMIN".equals(currentUser.getRole())) {
            return false; // 非ADMIN用户不能删除用户
        }
        
        // 检查要删除的用户是否是ADMIN角色（ADMIN用户不可以被删除）
        User userToDelete = userMap.get(username);
        if ("ADMIN".equals(userToDelete.getRole())) {
            return false; // ADMIN用户不能被删除
        }
        
        // 执行删除操作
        userMap.remove(username);
        saveUsers(); // 自动保存到文件
        return true;
    }
    /**
     * 查询所有用户
     * @return 所有用户列表
     */
    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userMap.values());
    }
    
    /**
     * 从文件加载用户数据
     */
    @Override
    public void loadUsers() {
        List<String> lines = FileUtil.readLines(Constants.USER_FILE_PATH);
        userMap.clear();
        
        for (String line : lines) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                continue;
            }
            
            // 解析用户数据，格式：用户名,密码,角色
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                User user = new User(username, password, role);
                userMap.put(username, user);
            }
        }
    }
    /**
     * 将用户数据保存到文件
     */
    @Override
    public void saveUsers() {
        List<String> lines = new ArrayList<>();
        
        // 将所有用户转换为字符串格式
        for (User user : userMap.values()) {
            String line = user.getUsername() + "," + user.getPassword() + "," + user.getRole();
            lines.add(line);
        }
        
        // 写入文件
        FileUtil.writeLines(Constants.USER_FILE_PATH, lines);
    }
    
}