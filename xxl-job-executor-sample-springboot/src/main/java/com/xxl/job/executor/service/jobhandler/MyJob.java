package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.myservice.DemoService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-02-28 13:29
 **/
@JobHandler(value = "myJob")
@Component
public class MyJob extends IJobHandler {
    @Autowired
    DemoService service;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(param);
        service.sout(Strings.isEmpty(param) ? "ada" : param);
        return null;
    }
}
