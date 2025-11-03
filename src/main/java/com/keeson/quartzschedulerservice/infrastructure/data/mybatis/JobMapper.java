package com.keeson.quartzschedulerservice.infrastructure.data.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keeson.quartzschedulerservice.domain.entity.domain.JobAndTrigger;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author Yhy
 * @create 2025/10/21 17:04
 * @describe
 */
@Component
public interface JobMapper {
    @Select({
            "<script>",
            "SELECT ",
            "   job_details.DESCRIPTION as jobDescription,",
            "   job_details.JOB_NAME,",
            "   job_details.JOB_GROUP,",
            "   job_details.JOB_CLASS_NAME,",
            "   cron_triggers.CRON_EXPRESSION,",
            "   cron_triggers.TIME_ZONE_ID,",
            "   qrtz_triggers.TRIGGER_NAME,",
            "   qrtz_triggers.TRIGGER_GROUP,",
            "   qrtz_triggers.TRIGGER_STATE",
            "FROM QRTZ_JOB_DETAILS job_details",
            "LEFT JOIN QRTZ_CRON_TRIGGERS cron_triggers",
            "   ON job_details.JOB_NAME = cron_triggers.TRIGGER_NAME",
            "  AND job_details.JOB_GROUP = cron_triggers.TRIGGER_GROUP",
            "LEFT JOIN QRTZ_TRIGGERS qrtz_triggers",
            "   ON qrtz_triggers.TRIGGER_NAME = job_details.JOB_NAME",
            "  AND qrtz_triggers.TRIGGER_GROUP = job_details.JOB_GROUP",
            "<where>",
            "   <if test='jobDescription != null and jobDescription != \"\"'>",
            "       AND job_details.DESCRIPTION LIKE CONCAT('%', #{jobDescription}, '%')",
            "   </if>",
            "   <if test='jobGroup != null and jobGroup != \"\"'>",
            "       AND job_details.JOB_GROUP LIKE CONCAT('%', #{jobGroup}, '%')",
            "   </if>",
            "</where>",
            "ORDER BY job_details.JOB_NAME ASC",
            "</script>"
    })
    IPage<JobAndTrigger> listJobs(Page<JobAndTrigger> page,
                                  @Param("jobDescription") String jobDescription,
                                  @Param("jobGroup") String jobGroup);
}
