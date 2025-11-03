package com.keeson.quartzschedulerservice.controller;

import cn.hutool.core.lang.Dict;
import com.keeson.common.domain.response.PageResponse;
import com.keeson.quartzschedulerservice.controller.common.ApiResponse;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import com.keeson.quartzschedulerservice.domain.entity.form.JobDubboForm;
import com.keeson.quartzschedulerservice.domain.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * Job Controller
 * </p>
 *
 * @author dnydys
 * @date Created in 2021-12-11 23:48
 */
@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 添加RPC任务
     *
     * @param form
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody @Valid JobDubboForm form) {
        try {
            jobService.addDubboJob(form);
            return new ResponseEntity<>(ApiResponse.msg("任务创建成功"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.msg("创建失败: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除任务
     *
     * @param interfaceName
     * @param methodName
     * @param jobGroupName
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam String interfaceName,
                                              @RequestParam String methodName,
                                              @RequestParam String jobGroupName) {
        try {
            JobDubboForm form = new JobDubboForm();
            form.setInterfaceName(interfaceName);
            form.setMethodName(methodName);
            form.setJobGroupName(jobGroupName);
            jobService.deleteJob(form);
            return ResponseEntity.ok(ApiResponse.msg("任务删除成功"));
        } catch (SchedulerException e) {
            return new ResponseEntity<>(ApiResponse.msg("删除失败: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 暂停任务
     *
     * @param interfaceName
     * @param methodName
     * @param jobGroupName
     * @return
     */
    @PutMapping("/pause")
    public ResponseEntity<ApiResponse> pause(@RequestParam String interfaceName,
                                             @RequestParam String methodName,
                                             @RequestParam String jobGroupName) {
        try {
            JobDubboForm form = new JobDubboForm();
            form.setInterfaceName(interfaceName);
            form.setMethodName(methodName);
            form.setJobGroupName(jobGroupName);
            jobService.pauseJob(form);
            return ResponseEntity.ok(ApiResponse.msg("任务已暂停"));
        } catch (SchedulerException e) {
            return new ResponseEntity<>(ApiResponse.msg("暂停失败: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 恢复任务
     *
     * @param interfaceName
     * @param methodName
     * @param jobGroupName
     * @return
     */
    @PutMapping("/resume")
    public ResponseEntity<ApiResponse> resume(@RequestParam String interfaceName,
                                              @RequestParam String methodName,
                                              @RequestParam String jobGroupName) {
        try {
            JobDubboForm form = new JobDubboForm();
            form.setInterfaceName(interfaceName);
            form.setMethodName(methodName);
            form.setJobGroupName(jobGroupName);
            jobService.resumeJob(form);
            return ResponseEntity.ok(ApiResponse.msg("任务已恢复"));
        } catch (SchedulerException e) {
            return new ResponseEntity<>(ApiResponse.msg("恢复失败: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改任务
     *
     * @param jobDescription
     * @param interfaceName
     * @param methodName
     * @param jobGroupName
     * @param cronExpression
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCron(@RequestParam String jobDescription,
                                                  @RequestParam String interfaceName,
                                                  @RequestParam String methodName,
                                                  @RequestParam String jobGroupName,
                                                  @RequestParam String cronExpression) {
        try {
            jobService.cronJob(new JobDubboForm(jobDescription, jobGroupName, cronExpression, interfaceName, methodName, null, null));
            return ResponseEntity.ok(ApiResponse.msg("Cron表达式修改成功"));
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.msg("修改失败: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 分页
     *
     * @param currentPage
     * @param pageSize
     * @param jobName
     * @param jobGroup
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> list(@RequestParam(required = false) Integer currentPage,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestParam(required = false) String jobName,
                                            @RequestParam(required = false) String jobGroup) {
        if (currentPage == null) currentPage = 1;
        if (pageSize == null) pageSize = 10;

        PageResponse<JobAndTrigger> pageResponse = jobService.list(currentPage, pageSize, jobName, jobGroup);
        return ResponseEntity.ok(ApiResponse.ok(Dict.create()
                .set("total", pageResponse.getTotal())
                .set("data", pageResponse.getList())));
    }

}
