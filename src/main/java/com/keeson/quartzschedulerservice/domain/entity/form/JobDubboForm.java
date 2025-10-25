package com.keeson.quartzschedulerservice.domain.entity.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 添加Dubbo定时任务
 *
 * @Author Yhy
 * @create 2025/10/24 17:07
 * @describe
 */
@Data
public class JobDubboForm {
    /**
     * 定时任务描述
     */
    @NotBlank(message = "任务描述不能为空")
    private String jobDescribe;
    /**
     * 定时任务组名(一般为dubbo接口的项目名称)
     */
    @NotBlank(message = "任务组名不能为空")
    private String jobGroupName;
    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    /**
     * Dubbo 接口全限定名
     */
    @NotBlank(message = "接口名不能为空")
    private String interfaceName;
    /**
     * 方法名(请保证接口名+方法名唯一)
     */
    @NotBlank(message = "方法名不能为空")
    private String methodName;
    /**
     * 参数类型（如 java.lang.String）
     */
    private List<String> paramTypes;

    /**
     * 参数值（与 paramTypes 一一对应）
     */
    private List<String> paramValues;
}
