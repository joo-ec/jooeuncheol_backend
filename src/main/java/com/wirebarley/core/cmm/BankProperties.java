package com.wirebarley.core.cmm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Component
public class BankProperties {
    public static final String ERR_CODE = " EXCEPTION OCCURRED";
    static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String GLOBALS_PROPERTIES_FILE = "classpath:" + FILE_SEPARATOR + "application.properties";

    public static String getProperty(String keyName) {
        String value = ERR_CODE;
        value = "99";

        Resource resources = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader())
                .getResource(GLOBALS_PROPERTIES_FILE);

        log.debug(GLOBALS_PROPERTIES_FILE + " : " + keyName);

        try (InputStream in = resources.getInputStream()) {
            Properties props = new Properties();
            props.load(new java.io.BufferedInputStream(in));
            value = props.getProperty(keyName).trim();
        } catch (FileNotFoundException fne) {
            log.debug(fne.getMessage());
        } catch (IOException ioe) {
            log.debug(ioe.getMessage());
        }
        return value;
    }

}
