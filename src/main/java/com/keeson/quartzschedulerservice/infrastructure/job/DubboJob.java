package com.keeson.quartzschedulerservice.infrastructure.job;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Dubbo调用任务配置
 *
 * @Author Yhy
 * @create 2025/10/25 10:41
 * @describe
 */
@Slf4j
public class DubboJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();

        String interfaceName = dataMap.getString("interfaceName");
        String methodName = dataMap.getString("methodName");
        List<String> paramTypes = JSONArray.parseArray(dataMap.get("paramTypes").toString(), String.class);
        List<String> paramValues = JSONArray.parseArray(dataMap.get("paramValues").toString(), String.class);

        try {
            GenericService dubbboGenericService = getGenericService(interfaceName);
            dubbboGenericService.$invoke(methodName, paramTypes.toArray(new String[0]), paramValues.toArray());
        } catch (Exception e) {
            log.error("DubboJob execution failed", e);
        }
    }

    /**
     * 获取dubbo服务
     *
     * @param interfaceName 接口名称
     * @return
     */
    private GenericService getGenericService(String interfaceName) {
        // 构建 Dubbo 泛化引用
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(interfaceName);
        reference.setGeneric("true");
        reference.setCheck(false); // 服务不存在时不立即报错
        reference.setTimeout(30000);
        reference.setRetries(0);

        // 可以根据你注册中心配置修改
        reference.setUrl(null); // 为空使用注册中心
        return reference.get();
    }
}
