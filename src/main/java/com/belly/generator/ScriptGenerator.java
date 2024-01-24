package com.belly.generator;

import cn.hutool.core.io.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * @author Belly
 * @version 1.1.0
 */
public class ScriptGenerator {
    public static void generator(String outputPath, String jarPath){

        //写入Linux脚本文件
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#!/bin/bash").append("\n");
        stringBuilder.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
        FileUtil.writeBytes(
                stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath);
        try {
            Set<PosixFilePermission> filePermissionSet = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath), filePermissionSet);
        } catch (IOException e) {
            System.out.println("添加可执行权限失败");
        }

        //写入Window脚本文件
        stringBuilder = new StringBuilder();
        stringBuilder.append("@echo off").append("\n");
        //%%* 第一个百分号代表转义
        stringBuilder.append(String.format("java -jar %s %%*", jarPath)).append("\n");
        FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath + ".bat");
    }
}
