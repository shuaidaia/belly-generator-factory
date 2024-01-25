package com.belly.generator;

import java.io.*;
import java.util.Map;

/**
 * @author Belly
 * @version 1.1.0
 *
 * 打包类
 */
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //clean后用maven打包
        //不同操作系统不同打包方式
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = winMavenCommand;

        //以空格拆分
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);
        Process process = processBuilder.start();

        //读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }


        //命令执行结束
        int exit = process.waitFor();
        System.out.println("命令执行结束，退出码：" + exit);

    }
}
