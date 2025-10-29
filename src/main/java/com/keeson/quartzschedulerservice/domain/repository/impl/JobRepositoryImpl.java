package com.keeson.quartzschedulerservice.domain.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import com.keeson.quartzschedulerservice.domain.repository.JobRepository;
import com.keeson.quartzschedulerservice.infrastructure.data.mybatis.JobMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author Yhy
 * @create 2025/10/28 15:06
 * @describe
 */
@Repository
@Slf4j
public class JobRepositoryImpl implements JobRepository {
    @Autowired
    private JobMapper jobMapper;


    @Override
    public IPage<JobAndTrigger> findAllJobs(Integer pageNum, Integer pageSize, String jobName, String jobGroup) {
        Page<JobAndTrigger> page = new Page<>(pageNum, pageSize);
        return jobMapper.listJobs(page, jobName, jobGroup);
    }
}
