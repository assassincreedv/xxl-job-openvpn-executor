package com.xxl.job.executor.sample.frameless.config;

import com.xxl.job.executor.sample.frameless.jobhandler.SampleXxlJob;
import com.xxl.job.core.executor.impl.XxlJobSimpleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author xuxueli 2018-10-31 19:05:43
 */
public class FrameLessXxlJobConfig {
    private static Logger logger = LoggerFactory.getLogger(FrameLessXxlJobConfig.class);


    private static FrameLessXxlJobConfig instance = new FrameLessXxlJobConfig();
    public static FrameLessXxlJobConfig getInstance() {
        return instance;
    }

    private XxlJobSimpleExecutor xxlJobExecutor = null;

//    private final OpenVPNConfig openVPNConfig = new OpenVPNConfig();
//
//    public OpenVPNConfig getOpenvpnConfig() { return openVPNConfig; }
//
//    public static class OpenVPNConfig {
//        private String ip;
//
//        private String port;
//
//        private String url;
//
//        public String getIp() {
//            return ip;
//        }
//
//        public void setIp(String ip) {
//            this.ip = ip;
//        }
//
//        public String getPort() {
//            return port;
//        }
//
//        public void setPort(String port) {
//            this.port = port;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }



    /**
     * init
     */
    public void initXxlJobExecutor() {

        // load executor prop
        Properties xxlJobProp = loadProperties("xxl-job-executor.properties");

        // init executor
        xxlJobExecutor = new XxlJobSimpleExecutor();
        xxlJobExecutor.setAdminAddresses(xxlJobProp.getProperty("xxl.job.admin.addresses"));
        xxlJobExecutor.setAccessToken(xxlJobProp.getProperty("xxl.job.accessToken"));
        xxlJobExecutor.setAppname(xxlJobProp.getProperty("xxl.job.executor.appname"));
        xxlJobExecutor.setAddress(xxlJobProp.getProperty("xxl.job.executor.address"));
        xxlJobExecutor.setIp(xxlJobProp.getProperty("xxl.job.executor.ip"));
        xxlJobExecutor.setPort(System.getenv("XXL_JOB_PORT") != null ? Integer.valueOf(System.getenv("XXL_JOB_PORT")) : Integer.valueOf(xxlJobProp.getProperty("xxl.job.executor.port")));
        xxlJobExecutor.setLogPath(xxlJobProp.getProperty("xxl.job.executor.logpath"));
        xxlJobExecutor.setLogRetentionDays(Integer.valueOf(xxlJobProp.getProperty("xxl.job.executor.logretentiondays")));

        // registry job bean
        xxlJobExecutor.setXxlJobBeanList(Arrays.asList(new SampleXxlJob()));

        // start executor
        try {
            xxlJobExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

//    public void initOpenVPNConfig() {
//        // load openvpn prop
//        Properties xxlJobProp = loadProperties("xxl-job-executor.properties");
//        openVPNConfig.setIp(xxlJobProp.getProperty("openvpn.ip"));
//        openVPNConfig.setPort(xxlJobProp.getProperty("openvpn.port"));
//        openVPNConfig.setUrl(xxlJobProp.getProperty("openvpn.url"));
//    }

    /**
     * destory
     */
    public void destoryXxlJobExecutor() {
        if (xxlJobExecutor != null) {
            xxlJobExecutor.destroy();
        }
    }


    public static Properties loadProperties(String propertyFileName) {
        InputStreamReader in = null;
        try {
            ClassLoader loder = Thread.currentThread().getContextClassLoader();

            in = new InputStreamReader(loder.getResourceAsStream(propertyFileName), "UTF-8");;
            if (in != null) {
                Properties prop = new Properties();
                prop.load(in);
                return prop;
            }
        } catch (IOException e) {
            logger.error("load {} error!", propertyFileName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close {} error!", propertyFileName);
                }
            }
        }
        return null;
    }

}
