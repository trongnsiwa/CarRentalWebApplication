/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author TrongNS
 */
public class MyContextListener implements ServletContextListener {
    
    private final String MAPPING_FILE = "/WEB-INF/MappingPage.properties";
    
    static final Logger LOGGER = Logger.getLogger(MyContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = new Properties();
        ServletContext context = sce.getServletContext();
        
        String realPath = context.getRealPath(MAPPING_FILE);

        InputStream inputStream = null;
        HashMap<String, String> map = new HashMap<>();

        try {
            inputStream = new FileInputStream(realPath);
            properties.load(inputStream);

            for (String name : properties.stringPropertyNames()) {
                map.put(name, properties.getProperty(name));
            }
        } catch (FileNotFoundException ex) {
            String message = ex.getMessage();
            LOGGER.debug("Error at MyContextListener _ FileNotFoundException : " + message);
        } catch (IOException ex) {
            String message = ex.getMessage();
            LOGGER.debug("Error at MyContextListener _ IOException : " + message);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                String message = ex.getMessage();
                LOGGER.debug("Error at MyContextListener _ IOException : " + message);
            }
        }

        context.setAttribute("MAPPING_LIST", map);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
