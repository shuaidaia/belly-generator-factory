package com.belly.generator.executor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.belly.generator.JarGenerator;
import com.belly.generator.ScriptGenerator;
import com.belly.generator.file.DynamicFileGenerator;
import com.belly.model.Meta;
import com.belly.model.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author Belly
 * @version 1.1.0
 * <p>
 * 生成器主类
 */
public class ExecutorGenerator {

    /**
     * 生成方法
     */
    public void generator() throws TemplateException, IOException, InterruptedException {
        //通过MetaManager类获取Meta对象
        Meta meta = MetaManager.getMetaObject();

        //得到项目根路径
        String rootPath = System.getProperty("user.dir");
        String outputPath =
                rootPath + File.separator + "generated" + File.separator + meta.getName();

        //判断文件路径是否存在，否则生成
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        //1.拷贝原始文件
        String copySource = copySource(meta, outputPath);

        //2.生成代码
        generateCode(meta, outputPath);

        //3.构建jar包
        String buildJarPath = buildJar(meta, outputPath);

        //4.封装脚本
        String buildScriptPath = buildScript(outputPath, buildJarPath);

    }

    /**
     * 拷贝原始文件
     *
     * @param meta
     * @param outputPath
     * @return
     */
    protected String copySource(Meta meta, String outputPath) {
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyTargetPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyTargetPath, false);

        //返回输出路径
        return sourceCopyTargetPath;
    }


    /**
     * 生成代码文件
     * @param meta
     * @param outputPath
     * @throws TemplateException
     * @throws IOException
     */
    protected void generateCode(Meta meta, String outputPath) throws TemplateException, IOException {
        //读取resources目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        //java包基础路径
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath =
                StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;

        //生成Meta类
        String inputFilePath;
        String outputFilePath;
        inputFilePath = inputResourcePath + File.separator + "templates/model/Meta.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/model/Meta.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //生成命令类
        //1.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //2.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //3.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //4.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //生成生成器类
        //DynamicFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/generator/DynamicFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicFileGenerator.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //StaticFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/generator/StaticFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticFileGenerator.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //ExecutorFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/generator/ExecutorFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/ExecutorFileGenerator.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //启动类MainExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/MainExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/MainExecutor.java";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);

        //生成pom文件
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.executor(inputFilePath, outputFilePath, meta);
    }

    /**
     * 构建jar包
     * @param meta
     * @param outputPath
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected String buildJar(Meta meta, String outputPath) throws IOException, InterruptedException {
        JarGenerator.doGenerate(outputPath);
        String format = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + format;
        return jarPath;
    }

    protected String buildScript(String outputPath, String jarPath){
        String shellPath = outputPath + File.separator + "generator";
        ScriptGenerator.generator(shellPath, jarPath);
        return shellPath;
    }


}
