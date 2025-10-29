package com.keeson.quartzschedulerservice.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;

/**
 * 查询定时任务
 *
 * @Author Yhy
 * @create 2025/10/28 15:00
 * @describe
 */
public interface JobRepository {

    /**
     * 查询所有的定时任务
     *
     * @return
     */
    IPage<JobAndTrigger> findAllJobs(Integer pageIndex, Integer pageSize, String jobName, String jobGroup);
}
