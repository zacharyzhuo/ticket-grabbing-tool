package com.zachary.ticketgrabbingtool.resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Properties;

public class LocalFileConfigProvider implements ExternalConfigProvider {

    private Logger logger = LoggerFactory.getLogger(LocalFileConfigProvider.class);

    private Properties properties;

    public String getString(String name) {
        try {
            if (properties == null) {
                properties = new Properties();

                Resource resource = new ClassPathResource("application.yml");

                InputStream is = null;

                try {
                    is = resource.getInputStream();
                    properties.load(is);
                } catch (Exception ex) {
                    logger.error(ExceptionUtils.getStackTrace(ex));
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }

            return (String) properties.getProperty(name);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));

            return null;
        }
    }

}
