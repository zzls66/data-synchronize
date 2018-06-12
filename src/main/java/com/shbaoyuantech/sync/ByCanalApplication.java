package com.shbaoyuantech.sync;

import com.google.common.eventbus.Subscribe;
import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByCanalApplication {

    private static Logger logger = LoggerFactory.getLogger(ByCanalApplication.class);

    private static Configuration config;

    static{
        try {
            config = Configuration.getInstance();
        }catch (Exception e){
            throw new BizException("error##ByCanalApplicationThreadPool, init env has an error.", e);
        }
    }

    public static void main(String[] args) {
        CanalInstance common = new CanalInstance(config.getCanalDestinationCommon(), config.getDatabaseCommon());
        ExceptionHandler commonHandler = new ExceptionHandler(common);
        startCanalInstanceThread(common, commonHandler);

        CanalInstance guowen = new CanalInstance(config.getCanalDestinationGuowen(), config.getDatabaseGuowen());
        ExceptionHandler guowenHandler = new ExceptionHandler(guowen);
        startCanalInstanceThread(guowen, guowenHandler);

        CanalInstance guoyi = new CanalInstance(config.getCanalDestinationGuoyi(), config.getDatabaseGuoyi());
        ExceptionHandler guoyiHandler = new ExceptionHandler(guoyi);
        startCanalInstanceThread(guoyi, guoyiHandler);
    }

    static void startCanalInstanceThread(CanalInstance canalInstance, ExceptionHandler exceptionHandler){
        Thread thread = new Thread(canalInstance);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.start();
    }

    //重启成功后，重置当前进程的尝试次数
    @Subscribe
    public void resetRetryTimes(Thread thread) {
        ExceptionHandler handler = (ExceptionHandler) thread.getUncaughtExceptionHandler();
        if(handler.getRetryTimes() < ExceptionHandler.MAX_RETRY_TIMES){
            logger.info("retry successful, reset retry times...");
            handler.setRetryTimes(ExceptionHandler.MAX_RETRY_TIMES);
        }
}
}
