package com.keeson.quartzschedulerservice.domain.service;


import com.keeson.common.domain.response.PageResponse;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import com.keeson.quartzschedulerservice.domain.entity.form.JobDubboForm;
import org.quartz.SchedulerException;

/**
 * <p>
 * Job Service
 * </p>
 *
 * @author dnydys
 * @date Created in 2021-12-11 23:48
 */
public interface JobService {

    /**
     * 添加并启动 Dubbo 定时任务
     *
     * @param jobDubboForm 表单参数 {@link JobDubboForm}
     * @throws Exception 创建失败异常
     */
    void addDubboJob(JobDubboForm jobDubboForm) throws Exception;

    /**
     * 删除 Dubbo 定时任务
     *
     * @param form 表单参数 {@link JobDubboForm}
     * @throws SchedulerException 删除失败异常
     */
    void deleteJob(JobDubboForm form) throws SchedulerException;

    /**
     * 暂停 Dubbo 定时任务
     *
     * @param form 表单参数 {@link JobDubboForm}
     * @throws SchedulerException 暂停失败异常
     */
    void pauseJob(JobDubboForm form) throws SchedulerException;

    /**
     * 恢复 Dubbo 定时任务
     *
     * @param form 表单参数 {@link JobDubboForm}
     * @throws SchedulerException 恢复失败异常
     */
    void resumeJob(JobDubboForm form) throws SchedulerException;

    /**
     * 重新配置 Dubbo 定时任务的 cron 表达式
     *
     * @param form 表单参数 {@link JobDubboForm}
     * @throws Exception 修改失败异常
     */
    void cronJob(JobDubboForm form) throws Exception;

    /**
     * 查询定时任务列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页条数
     * @return 定时任务列表
     */
    PageResponse<JobAndTrigger> list(Integer currentPage, Integer pageSize, String jobDescription, String jobGroup);
}
