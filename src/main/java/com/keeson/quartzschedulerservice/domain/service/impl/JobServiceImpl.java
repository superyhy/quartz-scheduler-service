package com.keeson.quartzschedulerservice.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keeson.common.domain.response.PageResponse;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import com.keeson.quartzschedulerservice.domain.entity.form.JobDubboForm;
import com.keeson.quartzschedulerservice.domain.entity.form.JobForm;
import com.keeson.quartzschedulerservice.domain.repository.JobRepository;
import com.keeson.quartzschedulerservice.domain.service.JobService;
import com.keeson.quartzschedulerservice.infrastructure.job.DubboJob;
import com.keeson.quartzschedulerservice.infrastructure.util.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Yhy
 * @create 2025/10/21 16:16
 * @describe
 */
@Service
@Slf4j
public class JobServiceImpl implements JobService {
    private final Scheduler scheduler;
    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(Scheduler scheduler, JobRepository jobRepository) {
        this.scheduler = scheduler;
        this.jobRepository = jobRepository;
    }

    /**
     * 添加并启动定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws Exception
     */
    @Override
    public void addJob(JobForm form) throws Exception {
        // 启动调度器
        scheduler.start();

        // 构建Job信息
        JobDetail jobDetail = JobBuilder.newJob(JobUtil.getClass(form.getJobClassName()).getClass()).withIdentity(form.getJobClassName(), form.getJobGroupName()).build();

        // Cron表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(form.getCronExpression());

        //根据Cron表达式构建一个Trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(form.getJobClassName(), form.getJobGroupName()).withSchedule(cron).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("【定时任务】创建失败！", e);
            throw new Exception("【定时任务】创建失败！");
        }
    }

    @Override
    public void addDubboJob(JobDubboForm jobDubboForm) throws Exception {
        // 启动调度器
        scheduler.start();

        JobDetail jobDetail = JobBuilder.newJob(DubboJob.class)
                .withIdentity(jobDubboForm.getInterfaceName() + ":" + jobDubboForm.getMethodName(), jobDubboForm.getJobGroupName())
                .withDescription(jobDubboForm.getJobDescribe())
                .usingJobData("interfaceName", jobDubboForm.getInterfaceName())
                .usingJobData("methodName", jobDubboForm.getMethodName())
                .usingJobData("paramTypes", JSONObject.toJSONString(jobDubboForm.getParamTypes()))
                .usingJobData("paramValues", JSONObject.toJSONString(jobDubboForm.getParamValues()))
                .storeDurably(true)
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobDubboForm.getCronExpression());
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withDescription(jobDubboForm.getJobDescribe())
                .withIdentity(jobDubboForm.getInterfaceName() + ":" + jobDubboForm.getMethodName(), jobDubboForm.getJobGroupName())
                .withSchedule(scheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("创建dubbo定时任务失败", e);
            throw new Exception("创建dubbo定时任务失败");
        }
    }

    /**
     * 删除定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException
     */
    @Override
    public void deleteJob(JobForm form) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName()));
        scheduler.unscheduleJob(TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName()));
        scheduler.deleteJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 暂停定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException
     */
    @Override
    public void pauseJob(JobForm form) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 恢复定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException
     */
    @Override
    public void resumeJob(JobForm form) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 重新配置定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws Exception
     */
    @Override
    public void cronJob(JobForm form) throws Exception {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(form.getCronExpression());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 根据Cron表达式构建一个Trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("【定时任务】更新失败！", e);
            throw new Exception("【定时任务】创建失败！");
        }
    }

    /**
     * 查询定时任务列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页条数
     * @return
     */
    @Override
    public PageResponse<JobAndTrigger> list(Integer currentPage, Integer pageSize, String jobName, String jobGroup) {
        IPage<JobAndTrigger> jobAndTriggerIPage = jobRepository.findAllJobs(currentPage, pageSize, jobName, jobGroup);
        return new PageResponse<>(jobAndTriggerIPage.getRecords(), jobAndTriggerIPage.getTotal());
    }
}
