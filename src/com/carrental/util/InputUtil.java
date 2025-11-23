package com.carrental.util;

import java.util.Scanner;

/**
 * 输入工具类，用于处理用户输入并提供错误处理
 */
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 获取整数输入
     * @param prompt 提示信息
     * @return 用户输入的整数
     */
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("输入错误，请输入一个有效的整数！");
            }
        }
    }

    /**
     * 获取浮点数输入
     * @param prompt 提示信息
     * @return 用户输入的浮点数
     */
    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("输入错误，请输入一个有效的数字！");
            }
        }
    }

    /**
     * 获取字符串输入
     * @param prompt 提示信息
     * @return 用户输入的字符串
     */
    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * 获取非空字符串输入
     * @param prompt 提示信息
     * @return 用户输入的非空字符串
     */
    public static String getNonEmptyString(String prompt) {
        while (true) {
            String input = getString(prompt);
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            } else {
                System.out.println("输入不能为空！");
            }
        }
    }

    /**
     * 关闭Scanner
     */
    public static void close() {
        scanner.close();
    }
}