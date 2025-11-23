package com.carrental.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类，提供通用的文件读写功能
 */
public class FileUtil {
    
    /**
     * 读取文件内容，返回每一行作为列表元素
     * @param filePath 文件路径
     * @return 文件内容列表
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        
        // 如果文件不存在，返回空列表
        if (!file.exists()) {
            return lines;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("读取文件失败: " + filePath);
            e.printStackTrace();
        }
        
        return lines;
    }
    
    /**
     * 将内容列表写入文件
     * @param filePath 文件路径
     * @param lines 要写入的内容列表
     * @return 写入成功返回true，否则返回false
     */
    public static boolean writeLines(String filePath, List<String> lines) {
        // 确保目录存在
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("写入文件失败: " + filePath);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 向文件追加一行内容
     * @param filePath 文件路径
     * @param line 要追加的内容
     * @return 追加成功返回true，否则返回false
     */
    public static boolean appendLine(String filePath, String line) {
        // 确保目录存在
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("追加内容到文件失败: " + filePath);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查文件是否存在
     * @param filePath 文件路径
     * @return 文件存在返回true，否则返回false
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * 创建文件（如果不存在）
     * @param filePath 文件路径
     * @return 创建成功或文件已存在返回true，否则返回false
     */
    public static boolean createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        
        // 确保目录存在
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try {
            return file.createNewFile();
        } catch (IOException e) {
            System.err.println("创建文件失败: " + filePath);
            e.printStackTrace();
            return false;
        }
    }
}