package com.carrental.model;

/**
 * 用户实体类，用于系统登录验证
 */
public class User {
    private String username;    // 用户名
    private String password;    // 密码（实际应用中应该存储加密后的密码）
    private String role;        // 角色（如管理员、普通用户等）
    
    /**
     * 构造方法
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    /**
     * 获取用户名
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取密码
     * @return 密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取角色
     * @return 角色
     */
    public String getRole() {
        return role;
    }
    
    /**
     * 设置角色
     * @param role 角色
     */
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * 验证密码是否正确
     * @param password 待验证的密码
     * @return 如果密码正确返回true，否则返回false
     */
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}