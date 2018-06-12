package com.shbaoyuantech.config;

import com.shbaoyuantech.commons.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public final class Configuration {
    private static Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static Configuration instance;

    private Properties props = new Properties();

    private Configuration() {}

    public synchronized static Configuration getInstance() throws Exception {
        if (instance == null) {
            String env = System.getenv().get("CNHUTONG_BINLOG_ENV");
            if (env == null) {
                throw new BizException("error##Configuration::getInstance env CNHUTONG_BINLOG_ENV param not found!");
            }

            String config = String.format("configuration.%s.properties", env);
            logger.debug("Configuration::getInstance Configuration file: " + config);

            try (InputStream in = Configuration.class.getClassLoader().getResourceAsStream(config)) {
                instance = new Configuration();
                instance.props.load(in);
            }
        }
        return instance;
    }

    public String getCanalHost() {
        return props.getProperty("canal.host");
    }

    public int getCanalPort() {
        return Integer.valueOf(props.getProperty("canal.port"));
    }

    public String getCanalDestinationCommon() {
        return props.getProperty("canal.destination.common");
    }

    public String getCanalDestinationGuowen() {
        return props.getProperty("canal.destination.guowen");
    }

    public String getCanalDestinationGuoyi() {
        return props.getProperty("canal.destination.guoyi");
    }

    public String getCanalUsername() {
        return props.getProperty("canal.username");
    }

    public String getCanalPassword() {
        return props.getProperty("canal.password");
    }

    public String getDatabaseCommon() {
        return props.getProperty("app.database.common");
    }

    public String getDatabaseGuowen() {
        return props.getProperty("app.database.guowen");
    }

    public String getDatabaseGuoyi() {
        return props.getProperty("app.database.guoyi");
    }

    public String getTables() {
        return props.getProperty("app.tables");
    }

    public int getInterval() {
        return Integer.valueOf(props.getProperty("app.interval"));
    }

    public int getBatchSize() {
        return Integer.valueOf(props.getProperty("app.batchSize"));
    }

    public String getMongoHost() { return props.getProperty("app.mongo.host"); }

    public int getMongoPort() { return Integer.valueOf(props.getProperty("app.mongo.port")); }

    public String getMongoDatebase() { return props.getProperty("app.mongo.datebase"); }
}

