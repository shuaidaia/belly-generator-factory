package com.belly;

import java.util.Scanner;

/**
 * @author ${mainTemplate.author!''}
 * @version 1.1.0
 *
 * ACM 多数之和输入模板
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("请输入元素个数：");
        Scanner scanner = new Scanner(System.in);

<#if loop>
        while (scanner.hasNext()) {
</#if>

            System.out.println("===============");
            // 读取输入元素个数
            int n = scanner.nextInt();

            // 读取数组
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            // 处理问题逻辑，根据需要进行输出
            // 示例：计算数组元素的和
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }

            System.out.println("${outputText!'sum= '}" + sum);

<#if loop>
        }
</#if>

        scanner.close();
    }
}
