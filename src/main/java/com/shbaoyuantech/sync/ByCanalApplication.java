package com.shbaoyuantech.sync;

import com.shbaoyuantech.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByCanalApplication {

    private static Logger logger = LoggerFactory.getLogger(ByCanalApplication.class);

    private static Configuration config = Configuration.getInstance();

    public static void main(String[] args) {
        startProcessingThread(config.getCanalDestinationCommon(), config.getDatabaseCommon());
        startProcessingThread(config.getCanalDestinationGuowen(), config.getDatabaseGuowen());
        startProcessingThread(config.getCanalDestinationGuoyi(), config.getDatabaseGuoyi());
    }

    private static void startProcessingThread(String destination, String database) {
        CanalInstance canalInstance = new CanalInstance(destination, database);
        ExceptionHandler exceptionHandler = new ExceptionHandler(canalInstance);
        startProcessingThread(canalInstance, exceptionHandler);

        logger.info("Sync thread [" + destination + "," + database + "] is processing...");
    }

    public static void startProcessingThread(CanalInstance canalInstance, ExceptionHandler exceptionHandler) {
        Thread thread = new Thread(canalInstance);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.start();
    }
}
