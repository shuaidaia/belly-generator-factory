package com.belly;

import com.belly.generator.executor.ExecutorGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author Belly
 * @version 1.1.0
 */
public class MainExecutor {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        ExecutorGenerator executorGenerator = new ExecutorGenerator();
        executorGenerator.generator();
    }
}
