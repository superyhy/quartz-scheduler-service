package com.keeson.quartzschedulerservice.infrastructure.job;

import cn.hutool.core.date.DateUtil;
import com.keeson.quartzschedulerservice.infrastructure.job.base.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

/**
 * <p>
 * Test Job
 * </p>
 *
 * @author dnydys
 * @date Created in 2021-12-11 23:48
 */
@Slf4j
public class TestJob implements BaseJob {

    @Override
    public void execute(JobExecutionContext context) {
        log.error("Test Job 执行时间: {}", DateUtil.now());
    }
}
