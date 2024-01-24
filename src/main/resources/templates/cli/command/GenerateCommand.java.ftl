package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.ExecutorFileGenerator;
import ${basePackage}.model.Meta;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * @author ${author!''}
 * @version 1.1.0
*/
<#-- 生成选项 -->
<#macro generateOption indent modelInfo>
${indent}@Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}", </#if>"--${modelInfo.fieldName}"}, arity = "0..1", <#if modelInfo.description??>description = "${modelInfo.description}", </#if>interactive = true, echo = true)
${indent}private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#macro>

<#-- 生成命令调用 -->
<#macro generateCommand indent modelInfo>
${indent}System.out.println("输入${modelInfo.groupName}配置：");
${indent}CommandLine commandLine = new CommandLine(${modelInfo.type}Command.class);
${indent}commandLine.execute(${modelInfo.allArgsStr});
</#macro>

<#macro argsList modelInfo>

</#macro>

@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {
<#list modelConfig.models as modelInfo>

    <#-- 有分组 -->
    <#if modelInfo.groupKey??>
    /**
     * ${modelInfo.groupName}
     */
    static Meta.${modelInfo.type} ${modelInfo.groupKey} = new Meta.${modelInfo.type}();

    <#-- 根据分组生成命令类 -->
    @Command(name = "${modelInfo.groupKey}")
    @Data
    public static class ${modelInfo.type}Command implements Runnable {
    <#list modelInfo.models as subModelInfo>
        <@generateOption indent="        " modelInfo=subModelInfo />
    </#list>

        @Override
        public void run() {
            <#list modelInfo.models as subModelInfo>
            ${modelInfo.groupKey}.${subModelInfo.fieldName} = ${subModelInfo.fieldName};
            </#list>
        }
    }
    <#else>
    <@generateOption indent="    " modelInfo=modelInfo />
    </#if>
</#list>

    <#-- 生成调用方法 -->
    public Integer call() throws Exception {
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        <#if modelInfo.condition??>
        if (${modelInfo.condition}) {
            <@generateCommand indent="            " modelInfo=modelInfo />
        }
        <#else>
        <@generateCommand indent="      " modelInfo=modelInfo />
        </#if>
        </#if>
        </#list>
        <#-- 填充数据模型对象 -->
        Meta Meta = new Meta();
        BeanUtil.copyProperties(this, Meta);
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        Meta.${modelInfo.groupKey} = ${modelInfo.groupKey};
        </#if>
        </#list>
        ExecutorFileGenerator.generator(Meta);
        return 0;
    }
}
