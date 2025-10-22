package com.keeson.quartzschedulerservice.domain.job;

import cn.hutool.core.date.DateUtil;
import com.keeson.quartzschedulerservice.domain.job.base.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

/**
 * <p>
 * Hello Job
 * </p>
 *
 * @author dnydys
 * @date Created in 2021-12-11 23:48
 */
@Slf4j
public class HelloJob implements BaseJob {

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Hello Job 执行时间: {}", DateUtil.now());
    }
}
