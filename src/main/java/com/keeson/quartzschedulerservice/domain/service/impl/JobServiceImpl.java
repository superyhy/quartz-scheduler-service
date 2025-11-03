package com.keeson.quartzschedulerservice.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keeson.common.domain.response.PageResponse;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import com.keeson.quartzschedulerservice.domain.entity.form.JobDubboForm;
import com.keeson.quartzschedulerservice.domain.repository.JobRepository;
import com.keeson.quartzschedulerservice.domain.service.JobService;
import com.keeson.quartzschedulerservice.infrastructure.job.DubboJob;
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
     * 添加Dubbo RPC任务
     *
     * @param jobDubboForm 表单参数 {@link JobDubboForm}
     * @throws Exception
     */
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

    @Override
    public void deleteJob(JobDubboForm form) throws SchedulerException {
        String jobName = form.getInterfaceName() + ":" + form.getMethodName();
        JobKey jobKey = JobKey.jobKey(jobName, form.getJobGroupName());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, form.getJobGroupName());

        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        }
    }

    @Override
    public void pauseJob(JobDubboForm form) throws SchedulerException {
        String jobName = form.getInterfaceName() + ":" + form.getMethodName();
        JobKey jobKey = JobKey.jobKey(jobName, form.getJobGroupName());
        if (scheduler.checkExists(jobKey)) scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(JobDubboForm form) throws SchedulerException {
        String jobName = form.getInterfaceName() + ":" + form.getMethodName();
        JobKey jobKey = JobKey.jobKey(jobName, form.getJobGroupName());
        if (scheduler.checkExists(jobKey)) scheduler.resumeJob(jobKey);
    }

    @Override
    public void cronJob(JobDubboForm form) throws Exception {
        String jobName = form.getInterfaceName() + ":" + form.getMethodName();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, form.getJobGroupName());
        JobKey jobKey = JobKey.jobKey(jobName, form.getJobGroupName());

        // 检查任务是否存在
        if (!scheduler.checkExists(triggerKey)) {
            throw new Exception("任务不存在");
        }

        // 获取旧触发器
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        if (oldTrigger == null) {
            throw new Exception("触发器不存在");
        }

        // 获取旧的 JobDetail
        JobDetail oldJobDetail = scheduler.getJobDetail(jobKey);
        if (oldJobDetail == null) {
            throw new Exception("任务详情不存在");
        }

        // ---------- 1️⃣ 修改 JobDetail 的描述信息 ----------
        JobBuilder jobBuilder = oldJobDetail.getJobBuilder();
        JobDetail newJobDetail = jobBuilder
                .withDescription(form.getJobDescribe()) // 更新描述
                .build();

        // ---------- 2️⃣ 修改 Trigger 的 Cron 表达式 ----------
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(form.getCronExpression());
        CronTrigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .forJob(newJobDetail)
                .build();

        // ---------- 3️⃣ 更新任务 ----------
        // 先删除旧任务（避免 JobDetail 不更新的问题）
        scheduler.deleteJob(jobKey);

        // 重新添加新 JobDetail + 新 Trigger
        scheduler.scheduleJob(newJobDetail, newTrigger);
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
