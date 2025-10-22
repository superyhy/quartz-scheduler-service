package com.keeson.quartzschedulerservice.infrastructure.data.mybatis;

import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Yhy
 * @create 2025/10/21 17:04
 * @describe
 */
@Component
public interface JobMapper {
    /**
     * 查询定时作业和触发器列表
     *
     * @return 定时作业和触发器列表
     */
    List<JobAndTrigger> list();
}
