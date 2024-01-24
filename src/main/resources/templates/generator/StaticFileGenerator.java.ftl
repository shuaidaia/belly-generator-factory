package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

/**
 * @author ${author!''}
 * @version 1.1.0
 *
 * 静态文件生成类
 */
public class StaticFileGenerator {

    /**
     * 拷贝文件
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}