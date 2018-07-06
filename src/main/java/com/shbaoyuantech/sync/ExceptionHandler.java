package com.shbaoyuantech.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private static final int RETRY_INTERVAL = 5000;

    private static final int MAX_RETRY_TIMES = 3;

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
    public void uncaughtException(Thread t, Throwable throwable) {
        ExceptionHandler handler = (ExceptionHandler) t.getUncaughtExceptionHandler();

        if (isLastRetry(handler)) {
            logger.error("error##ByCanalApplication, the last retry is unsuccessful, canalInstance is" + canalInstance, throwable);
            return;
        }

        try {
            Thread.sleep(RETRY_INTERVAL);
        } catch (InterruptedException e) {
            logger.error("Internal Error: " + e);
            return;
        }

        logger.info("ByCanalApplication canalInstance:" + canalInstance + "retry, remainder is " + retryTimes);

        handler.setRetryTimes(handler.getRetryTimes() - 1);
        ByCanalApplication.startProcessingThread(canalInstance, handler);
    }

    public void resetMaxRetryTimes() {
        retryTimes = MAX_RETRY_TIMES;
    }

    private static boolean isLastRetry(ExceptionHandler handler){
        return handler.getRetryTimes() <= 0;
    }

}
