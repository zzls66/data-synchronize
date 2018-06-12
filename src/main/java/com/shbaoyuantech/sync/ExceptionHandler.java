package com.shbaoyuantech.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private static int RETRY_INTERVAL = 5000;

    public static int MAX_RETRY_TIMES = 3;

    private CanalInstance canalInstance;
    private Integer retryTimes = MAX_RETRY_TIMES;

    public ExceptionHandler(CanalInstance canalInstance) {
        this.canalInstance = canalInstance;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ExceptionHandler handler = (ExceptionHandler)t.getUncaughtExceptionHandler();

        if(isLastRetry(handler)){
            logger.error("error##ByCanalApplication, the last retry is unsuccessful, canalInstance is" + canalInstance, e);
        }

        try {
            Thread.sleep(RETRY_INTERVAL);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        if(!isLastRetry(handler)){
            logger.info("ByCanalApplication canalInstance:" + canalInstance + "retry, remainder is " + retryTimes);

            handler.setRetryTimes(handler.getRetryTimes() - 1);
            ByCanalApplication.startCanalInstanceThread(canalInstance, handler);
        }
    }

    private static boolean isLastRetry(ExceptionHandler handler){
        return handler.getRetryTimes() <= 0;
    }

}
